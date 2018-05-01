/**
 * the app has two controllers: mainCtrl - signin/sign up
 * 							    mainPageCtrl - all the other ops
 */
		OTalk = angular.module('mainApp',[]);

			//the controller of the signup/signin
			OTalk.controller('mainCtrl',['$scope','$http','$window',function($scope,$http,$window){
					
				$scope.username = ""; //input
				$scope.password = ""; //input
				$scope.loginResult = ""; //result message of login
				$scope.isSignInVisable=true;
				$scope.isSignUpVisable=false;
				$scope.isMainPageShown=false;
				$scope.confirmMes="";
				$scope.nickNameIsReady="";
				
				$scope.child = {} //in order to access child controller
				
				
				
				var LOGIN = 1;//at load time checks if the session is open
				$http.get("http://localhost:8080/webProject/SessionManager",
						{params: {"cmd":LOGIN}})
			    .success(function (response) {
			    	var res = true;
			    		if(response.queryResult == res){
			    			//retrieve back the session info - username/nickname

			    			$scope.userIsReady = response.username; 
			    			$scope.nickNameIsReady = response.nickName;
			    			$scope.isSignInVisable=false;
			    			$scope.isMainPageShown=true;
			    			$scope.channelnameshow = false;
			    		}
			    	});
				
				var LOGOUT = 2;
				$scope.logOut = function(){ //logout - closes the session
					$http.get("http://localhost:8080/webProject/SessionManager",
							{params: {"cmd":LOGOUT}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.queryResult == res){
				    			websocket.close();//closes the websocket
				    			$window.location.reload(); // return to loginpage
				    			$scope.$parent.isSignInVisable=true;
				    			$scope.$parent.isMainPageShown=false;
				    			
				    		}
				    	});
				}
				
				$scope.returnToMainPage = function(){ //click on the 'Otalk' logo return the user to main page
					$scope.child.isMainPageShown = true;
					$scope.child.isSearchShown = true;
					$scope.child.isChatShown = false;
					$scope.child.isReplyShown = false;
					$scope.child.channelchannel ='';
					var chat = document.getElementById("chatChat");
					chat.className = "container fill ChatBG ng-hide col-sm-" + 8;
					var last = document.getElementById("last");
					last.className = "ChatBG col-sm-"+2;
					document.getElementById('chatInput').style.width = '65%';

					
				}
				$scope.logIN = function(){ // takes the input at login page and log the user in
					$http.get("http://localhost:8080/webProject/LoginServlet", 
							{params:{"username": $scope.username, "password": $scope.password}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.logInResult == res){
				    			//changeLocation($scope.username);
				    			//$scope.loginMessage = response.logInResult;
				    			$scope.nickNameIsReady = response.usr.nickName;
				    			$scope.userIsReady = $scope.username;
				    			$scope.isSignInVisable=false;
				    			$scope.isMainPageShown=true;
				    			$scope.channelnameshow = false;
				    		}
				    		else
				    			$scope.loginMessage = "incorrect username/password";
				    	})
				    	.then(function(respone){
				    		$scope.loginResult = "error logging in"
					});
					
							
				};	
				
				$scope.backToSignIn = function(){ //back from sign up
					$scope.isSignInVisable = true;
					$scope.isSignUpVisable = false;
				}	
				
				$scope.signUpView = function(){ // enter sign up mode
					$scope.isSignInVisable=false;
					$scope.isSignUpVisable=true;
				};
				
				$scope.confirmPass = function(){
					if ($scope.sConfirm != $scope.sPassword) { 
						   
						$scope.confirmMes="Password don't match";
						}
					else{
						$scope.confirmMes="";
					}
				}
				//sign up inputs
				$scope.sUsername ="";
				$scope.sPassword ="";
				$scope.sConfirm ="";
				$scope.sNickname ="";
				$scope.sImage ="";
				$scope.textEntered="";
				$scope.signUP = function(){
					//validations
					if($scope.sUsername =="" ||
					$scope.sPassword == "" ||
					$scope.sConfirm == "" ||
					$scope.sNickname == ""){
						
						$scope.confirmMes = "Illegal input";
						return;
					}
					var data = {
							username: $scope.sUsername,
							password: $scope.sPassword,
							nickName:$scope.sNickname,
							imageUrl: $scope.sImage,
							description:$scope.textEntered,
					};
					var myData = JSON.stringify(data);//sign up request - should've been done in POST :-/
					$http.get("http://localhost:8080/webProject/SignUpServlet", 
							{params:{"data":myData}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.queryResult == res){
				    			//change to main page view
				    			$scope.isSignUpVisable=false;
				    			$scope.isMainPageShown=true;
				    			$scope.channelnameshow = false;
				    			$scope.userIsReady = $scope.sUsername;
				    			$scope.nickNameIsReady = $scope.sNickname;
				    			$scope.imgimg = [{
				    				src :"'" + $scope.sImage +"'"
				    			}];
				    		}
				    		else
				    			$scope.confirmMes = "Illegal input";
				    	})
				    	.then(function(respone){
				    		$scope.confirmMes = "Username/Nickname exists"
					});
				};
				/*$scope.remainingDesChars = function(val){
					  $scope.chrLength = val;
					}*/
				
		}]);
	
		
		
		
/*********************************************************************************************************/
		
		//second controller
		OTalk.controller('mainPageController', ['$scope','$http','$window', function($scope,$http,$window) {
				
				var CHANNELSLIST = 1; //commands
				var ADDCHANNEL = 2;
				var SUBSCRIBE = 3;
				var UNSUBSCRIBE = 4;
			    var PARTICIPANTS = 5;
				var USERPUBLICCHANNELS =6;
				

				$scope.isMainPageShown = true;
				$scope.isSearchShown = true;
				$scope.isChatShown = false;
				$scope.isReplyShown = false;
			    $scope.query = "";//this variable will hold the user's query
				$scope.publicChannels = [];
				$scope.privateChannels = [];
				$scope.records=[];
				$scope.tmpRecords = [];
				var helper = [];
				$scope.currentThread = [];
				$scope.currentReplys = [];
				$scope.currentParent = "";
				
				
				//with this, the mainCtrl can access this controller
				var parentScope = $scope.$parent;
				parentScope.child = $scope;
				
				var PRIVATE = true;
				var PUBLIC = false;
				$scope.chatMode = "";
				//when main page loads this request get the public channels from the server
				$http.get("http://localhost:8080/webProject/ChannelsManager",
						{params:{"cmd":CHANNELSLIST}}) 
				.success(function(response) {
				   var res = true;
				   if(response.result == true){
					   
				   
				   helper = response.data;
				   if(helper != undefined){
					   for (var i = 0; i < helper.length; i++) { //saves the channels
							$scope.records.push(helper[i]);
						}
					   for(i in $scope.records){
							$scope.tmpRecords.push($scope.records[i]);
						}
				   	}
				   }
				});
				
				
				$scope.$parent.$watch('userIsReady', function() { //wait for userIsReady to be updated
					if($scope.$parent.userIsReady == undefined){
						return;
					}//then when it does - bring the private/public channels of the user
					else if($scope.$parent.userIsReady.length != 0){
						
						 
						 $http.get("http://localhost:8080/webProject/ChannelsManager",
									{params:{"cmd":USERPUBLICCHANNELS,"username":$scope.$parent.userIsReady}}) 
							.success(function(response) {
								
							   helper = response.subscriptions;
							   if(helper!=null){
								   for (var i = 0; i < helper.length; i++) {
									   var channelObj = {};
									   /*
									    * name: name of the channel
									    * notif: number of notifs
									    * mention: was he mentioned on that channel
									    * */
									   channelObj.name = helper[i].channelName;
									   channelObj.notif = helper[i].notif;
									   channelObj.mention = helper[i].mention;
									   $scope.publicChannels.push(channelObj); //array to save the user public channels
									   $scope.useruseruser = $scope.$parent.userIsReady;
									   $scope.nicknamenickname = $scope.$parent.nickNameIsReady;
									}
								   
							   }
							   
								   cconnect($scope.$parent.userIsReady);//conncet to the websocket

							   
							});
						 //bring public channels
						 var USERPRIVATECHATS = 7;
						 $http.get("http://localhost:8080/webProject/ChannelsManager",
									{params:{"cmd":USERPRIVATECHATS,"username":$scope.$parent.userIsReady}}) 
							.success(function(response) {
								
							   helper = response.privateChats;
							   if(helper!=null){
								   for (var i = 0; i < helper.length; i++) {
									   var channelObj = {};
									   channelObj.name = helper[i].other;
									   channelObj.notif = 
										   	helper[i].notifTo == $scope.$parent.userIsReady? helper[i].notif:0;
										$scope.privateChannels.push(channelObj);
										
									}
								   
							   }
							   $scope.bodyStyle = {background: "url(js/BG.jpg) background-position: 150px 0px"};
							});
						 
						 //get img of the user
						 $http.get("http://localhost:8080/webProject/ChannelsManager",
									{params:{"cmd":13,"username":$scope.$parent.userIsReady 
										}}) 
							.success(function(response) {
								
								$scope.imgimg = [{
				    				src : response.imgUrl ,
				    			}];
							});
						 
						 
					 }
					
			    });
				$scope.searchBy = true; //indicates which type of search is used
				$scope.searchOption = "Channel Name";
				
				$scope.searchSite = function(){
					if($scope.searchBy){
						$scope.search();
					}
					else{
						$scope.searchByNicknames();
					}
				}
				
				$scope.result = $scope.tmpRecords;
				
				$scope.search = function(){
					
				    if (!$scope.query || $scope.query.length == 0){
				    	$scope.result = $scope.tmpRecords;
					}else{
					    var qstr = $scope.query.toLowerCase();
						$scope.result = [];
						for (x in $scope.tmpRecords){
							//check for a match (up to a lowercasing difference)
							/*
							 * tmpRecords contains this:
							 * channel {channel name & channel descreption}
							 * participants [array of that channel participants nicknames]
							 * */
							if ($scope.tmpRecords[x].channel.channelName.toLowerCase().match(qstr))
							{
								$scope.result.push($scope.tmpRecords[x]); //add record to search result
							}
						}
				   }
				};
				//search by nicknames
				$scope.searchByNicknames = function(){
					if (!$scope.query || $scope.query.length == 0){
				    	$scope.result = $scope.tmpRecords;
					}
					else{
					    var qstr = $scope.query.toLowerCase();
						$scope.result = [];
						
						for (x in $scope.records){
							var channelUsers = [];
							channelUsers = $scope.records[x].participants;
							if(channelUsers == undefined)
								continue;
							var stringPart = JSON.stringify(channelUsers);//cross the json of the nicknames of the participants and search for match
							
							if(stringPart.toLowerCase().match(qstr)){
								$scope.result.push($scope.tmpRecords[x]);
							}
							
						}
				   }
				};
				
				$scope.subscribeToPublic = function(x){//subscribe
					//insert subscription to the database
					 $http.get("http://localhost:8080/webProject/ChannelsManager",
								{params:{"cmd":SUBSCRIBE,"subscriber":$scope.$parent.userIsReady, 
									"nickName":$scope.$parent.nickNameIsReady,"channelName":x}}) 
						.success(function(response) {
							if($scope.publicChannels != undefined){
								//validation
								for (var i = 0 ; i < $scope.publicChannels.length; i++) {
									if($scope.publicChannels[i].name == x ){
										return;
									}
								}
							}
							//input
							var channelObj = {};
							channelObj.name = x;
							channelObj.notif = 0;
							$scope.publicChannels.push(channelObj);
							
							
							 
							
							return;
						});
						/*.then(function(error){
							$scope.testtest=$scope.location.host;
						});*/
					
					
				};
				$scope.unsubscribe2 = function(){//unsubscribe button which is inside a channel chat
					$scope.isReplyShown = false; //closes the chat window and calls a function to remove the chat from the list
					$scope.isChatShown = false;
					$scope.isSearchShown = true;
					$scope.currentThread = [];
					$scope.channelchannel ='';
					
					if($scope.chatMode == PUBLIC){
						$scope.unSubscribeToPublic($scope.currentChannel);
					}else{
						$scope.unSubscribeToPrivate($scope.currentPrivateChannel);
					}
					if($scope.currentChannel != undefined)
						$scope.currentChannel = "";
					if($scope.currentPrivateChannel != undefined)
						$scope.currentPrivateChannel = "";
					$scope.chatMode = "";
				}
				$scope.unSubscribeToPublic = function(x){
					//removes channel from db and from ui
					
					 $http.get("http://localhost:8080/webProject/ChannelsManager",
								{params:{"cmd":UNSUBSCRIBE,"subscriber":$scope.$parent.userIsReady, 
									"channelName":x}}) 
						.success(function(response) {
							
							var tmpArray = [];
							//iterte over the array and remove the wanted channel
							for (var i = $scope.publicChannels.length - 1 ; i >= 0; i--) {
								if($scope.publicChannels[i] != undefined){
									if($scope.publicChannels[i].name == x ){
										
										delete $scope.publicChannels[i];
									}
									//push everything except the removed channel in order to keep 
									//the array consistent
									if($scope.publicChannels[i] != undefined){
										tmpArray.push($scope.publicChannels[i]);
									}
								}
							}
							tmpArray.reverse();//reverse back to retrieve the order
							$scope.publicChannels = tmpArray;
						});
				};
				var UNSUBSCRIBE_PRIVATE = 9;
				$scope.unSubscribeToPrivate = function(x){ //unsubscribe from private chat
					
					
					 $http.get("http://localhost:8080/webProject/ChannelsManager",
								{params:{"cmd":UNSUBSCRIBE_PRIVATE,"username":$scope.$parent.userIsReady, 
									"other":x}}) 
						.success(function(response) {
							
							var tmpArray = [];
							for (var i = $scope.privateChannels.length - 1 ; i >= 0; i--) {
								if($scope.privateChannels[i] != undefined){
									if($scope.privateChannels[i].name == x ){
										
										delete $scope.privateChannels[i];
									}
									if($scope.privateChannels[i] != undefined){
										tmpArray.push($scope.privateChannels[i]);
									}
								}
							}
							tmpArray.reverse();
							$scope.privateChannels = tmpArray;
							sendMessageDeletePrivate( $scope.$parent.userIsReady, x);
							
						});
				};
				
				$scope.subscribed = function(x){//used to show subscribe/unsubscribe button
					for (var i = 0; i < $scope.publicChannels.length; i++) {
						if($scope.publicChannels[i].name == x ){
							return false;
						}
					}
					return true;
				}
				$scope.unSubscribed = function(x){//used to show subscribe/unsubscribe button
					for (var i = 0; i < $scope.publicChannels.length; i++) {
						if($scope.publicChannels[i].name == x ){
							return true;
						}
					}
				}
				//creates new channel
				$scope.createNewChannel = function(){
					$http.get("http://localhost:8080/webProject/ChannelsManager", 
							{params:{"cmd": ADDCHANNEL, "username": $scope.$parent.userIsReady,
								"nickName":$scope.$parent.nickNameIsReady,"channelName":$scope.newChannelName, "channelDescription":$scope.newChannelDes}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.result == res){
				    			//makes the channel object and add it to the public channels of the user
				    			//the user who adds channel will be subscribed auto to that channel
				    			var channelObj = {};
				    			channelObj.name = $scope.newChannelName;
								channelObj.notif = 0;
				    			$scope.publicChannels.push(channelObj);
				    			sendMessageNewChannel($scope.newChannelName, $scope.$parent.nickNameIsReady,$scope.newChannelDes);
				    			
				    			
				    		}
				    		else
				    			$scope.testtest="failed";
				    	})
				};
				
				//when a user adds channel - it will be added to the ui of all the others
				// by adding that channel to every user records
				//this function used when a special message recieved from the websocket
				function updateCurrentRecords(user,channel,descreption){
					var channelObj ={};
					var channelInfo = {};
					var currentUsers = [];
					
					channel.channelName = channel;
					channel.description = descreption;
					currentUsers.push(user);
					
					channelObj.participants = currentUsers;
					channelObj.channel = channelInfo;
					
					$scope.records.push(channelObj);
					$scope.tmpRecords.push(channel);
					$scope.$apply();
					
				
				}
				$scope.allMessages = [];
				$scope.chatMessages = [];
				var GET_CHAT_MESSAGES = 1;
				//when a user clicks a public channel
				$scope.connect = function(channel){
					$scope.currentThread = {};
					if($scope.isReplyShown){
						var chat = document.getElementById("chatChat");
						chat.className = "container fill ChatBG ng-hide col-sm-" + 8;
						var last = document.getElementById("last");
						last.className = "ChatBG col-sm-"+2;
						document.getElementById('chatInput').style.width = '65%';
					}
					channel.notif = 0;
					channel.mention = false;
					//cmd 12 for removing the user's notifications from the server
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":12,"username":$scope.$parent.userIsReady,
								"channelName":channel.name,"notif":0}})
				    .success(function (response) {});
					//removing mention-notification from the server
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":11,"username":$scope.$parent.userIsReady,
								"channelName":channel.name}})
				    .success(function (response) {});
					$scope.chatMode = PUBLIC; //chatMode used to specify in which mode the user is
					$scope.currentChannel = channel.name;
					$scope.channelchannel = "#" + $scope.currentChannel; //set the header
					$scope.currentPrivateChannel = "";
					$scope.isSearchShown = false;
					$scope.isChatShown = true;
					$scope.$parent.channelnameshow = true;
					$scope.allMessages = [];
					$scope.chatMessages = [];
					//brings messages of this channel from the time the user subscribed
					//the messages is selected due to it's 'last modified' date
					$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
							{params: {"cmd":GET_CHAT_MESSAGES,"channel":$scope.currentChannel,
								"username":$scope.$parent.userIsReady}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.queryResult == res){
				    			$scope.allMessages = response.data;
				    			/**$scope.chatMessages = response.data;*******************************/
				    			$scope.clicksclicks = 1;
				    			$scope.getTenMessages();
				    			
				    			//appeandMessages($scope.chatMessages);
				    			
				    		}
				    	});
					
					help();//empty the chat input
	        		//chatConsole.scrollTop = chatConsole.scrollHeight;
	        		
					
	            	
	            }
	            
				$scope.getTenMessages = function(){
					$scope.chatMessages = [];
					
					for(var k = $scope.allMessages.length - 1; k >=$scope.allMessages.length - 10 * $scope.clicksclicks;k--){
						if($scope.allMessages[k] != undefined){
							$scope.chatMessages.push($scope.allMessages[k]);
						}
						else{
							break;
						}
					}
					$scope.clicksclicks++;
					$scope.chatMessages.reverse();
				}
				
				var INSERT_PUBLIC_MESSAGE = 5;
				$scope.sendMessagee = function(){ //upon click or enter the message is sent
					if($scope.chatUserInput == ""){
						return;
					}
					//check if it;s public mode
					if($scope.chatMode == PUBLIC){
					//sent the message to the db
					$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
							{params: {"cmd":INSERT_PUBLIC_MESSAGE,"channel":$scope.currentChannel,
								"username":$scope.$parent.userIsReady,"content":$scope.chatUserInput,
								"nickName":$scope.nickNameIsReady,"imgUrl":$scope.imgimg[0].src}})
				    .success(function (response) {
				    	var res = true;//retrieve the message with it's id
				    		if(response.queryResult == res){
				    			response.pMessage.nickName = $scope.nickNameIsReady;
				    			//sends message through the websocket to all the users of the channel
				    			sendMessage(response.pMessage,$scope.$parent.userIsReady,$scope.currentChannel);
				    			
				    		}
				    	});
					}
					else{
						//just like above but private mode chat
						var ADD_PRIVATE_CHAT = 8;
						var INSERT_PRIVATE_MESSAGE = 6;
						if($scope.chatMessages.length == 0){//if it's the first message
							//then create private chat window
							$http.get("http://localhost:8080/webProject/ChannelsManager",
									{params: {"cmd":ADD_PRIVATE_CHAT,"other":$scope.currentPrivateChannel,
										"username":$scope.$parent.userIsReady}})
						    .success(function (response) {
						    	var res = true;
						    		if(response.result != res){
						    					alert("Cant Open PrivateChat");		    			
						    		}else{
						    			var channelObj = {};
						    			channelObj.name = $scope.currentPrivateChannel;
						    			channelObj.notif = 0;
						    			$scope.privateChannels.push(channelObj);
						    		}
						    	});
						}
						var privateMessage = {//prepare the message in json before send
								username : $scope.$parent.userIsReady,
								other : $scope.currentPrivateChannel,
								content:$scope.chatUserInput,
								nickName : $scope.nickNameIsReady,
								imgUrl : $scope.imgimg[0].src
						};
						pprivateMessage = JSON.stringify(privateMessage);
						//sends the message to db
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":INSERT_PRIVATE_MESSAGE,"message":pprivateMessage}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			response.prMessage.nickName = $scope.nickNameIsReady;
					    			//sends private message to websocket
					    			sendMessagePrivate(response.prMessage,$scope.$parent.userIsReady,$scope.currentPrivateChannel);
					    			
					    		}
					    	});
					}
				}
				var INSERT_PUBLIC_REPLY = 7;
				var INSERT_PPRIVATE_REPLY = 8;
				//send reply - again, there are two modes, private/public
				$scope.sendReply = function(){
					if($scope.replyUserInput == ""){
						return;
					}
					if($scope.chatMode == PUBLIC){//public mode:
						var myReply = {	//json format of the message before send to server/db
		        				username: $scope.$parent.userIsReady,
		        				channelName: $scope.currentChannel,
		        				content: $scope.replyUserInput,
		        				isReply: true,
		        				//replyTo is the parent message entity of this reply
		        				//we need it's id 
		        				replyTo: $scope.currentThread[$scope.currentThread.length - 1].messageID,
		        				nickName : $scope.nickNameIsReady,
								imgUrl : $scope.imgimg[0].src
		        		};
						var replyData = JSON.stringify(myReply);
						var parentMessage;
						//currentParent is the first father of the opened thread
						//this validation is:
						/**
						 * when we open a message thread (clicking reply)
						 * it becomes the father of reply threads
						 * if we encountered reply on reply on reply ...
						 * we keep tracking of the vary first father of the threads in order
						 * to update it's last modified time
						 * 
						 * this | validation is: if it's the first thread - send null parent to the server
						 * 		V
						 * because the local father of the reply will be updated anyway
						 */
						if($scope.currentParent == $scope.currentThread[$scope.currentThread.length - 1]){
							parentMessage = null;
						}
						else{
							parentMessage = $scope.currentParent;
						}
						//saves the reply to db
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":INSERT_PUBLIC_REPLY, "reply":replyData,
											"parentMessage":JSON.stringify(parentMessage)}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			$scope.replyUserInput = $scope.currentReplyInput;
					    			response.pMessage.nickName = $scope.nickNameIsReady;
					    			//sends reply through websocket
					    			sendMessageReply(response.pMessage,$scope.$parent.userIsReady,
					    					$scope.currentChannel,$scope.currentParent);
					    		}
					    	});
					}else{//else in private mode
						var myReply = {
		        				username: $scope.$parent.userIsReady,
		        				other: $scope.currentPrivateChannel,
		        				content: $scope.replyUserInput,
		        				isReply: true,
		        				replyTo: $scope.currentThread[$scope.currentThread.length - 1].messageID,
		        				nickName : $scope.nickNameIsReady,
								imgUrl : $scope.imgimg[0].src
		        		};
						var replyData = JSON.stringify(myReply);
						var parentMessage;
						if($scope.currentParent == $scope.currentThread[$scope.currentThread.length - 1]){
							parentMessage = null;
						}
						else{
							parentMessage = $scope.currentParent;
						}
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":INSERT_PPRIVATE_REPLY, "reply":replyData,
											"parentMessage":JSON.stringify(parentMessage)}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			$scope.replyUserInput = $scope.currentReplyInput;
					    			response.prMessage.nickName = $scope.nickNameIsReady;
					    			sendMessagePrivateReply(response.prMessage,$scope.$parent.userIsReady,$scope.currentPrivateChannel,$scope.currentParent);
					    		}
					    	});
					}
					
				}
				
				var GET_NICKNAME = 9;
				//reply to this - opens reply window
				$scope.replyToThis = function(message,flag){
					
					var chat = document.getElementById("chatChat");
					chat.className = "container ChatBG fill col-sm-" + 6;
					var last = document.getElementById("last");
					last.className = "ChatBG col-sm-"+4;
					document.getElementById('chatInput').style.width = '45%';
					
					if(!flag)
						$scope.currentThread = [];
					$scope.currentThread.push(message);
					$scope.currentParent = message;
					$scope.currentReplys = [];
					//makeReplyThread(data);
					//makeReplyThread();
					$scope.isReplyShown = true;
					
					
					if($scope.chatMode == PUBLIC){
						var GET_MESSAGE_REPLIES = 2;
						//gets replys of the message thread
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":2, "message":$scope.currentThread[$scope.currentThread.length - 1]}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			;
					    			$scope.replyUserInput = "@" + response.nickname + " ";
					    			$scope.currentReplyInput = $scope.replyUserInput;
					    			if(response.data != undefined){
					    				$scope.currentReplys = response.data;
					    				
					    			}
					    			
					    			//sendMessage(response.pMessage,$scope.$parent.userIsReady,$scope.currentChannel);
					    		}
					    	});
					}else{ //private mode
						var GET_PRIVATE_REPLIES = 4;
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":4, "message":$scope.currentThread[$scope.currentThread.length - 1],
									"username":$scope.userIsReady,"other":$scope.currentPrivateChannel}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			if(response.data != undefined){
					    				$scope.currentReplys = response.data;
					    				
					    			}
					    			$scope.replyUserInput = "@" + response.nickname + " ";
					    			//sendMessage(response.pMessage,$scope.$parent.userIsReady,$scope.currentChannel);
					    		}
					    	});
					}
					//$scope.$apply();
	            };
				
				$scope.replyToReply = function(message){//upon click on the reply input
					
					//change view and saves the previois thread
					var chat = document.getElementById("chatChat");
					chat.className = "container fill ChatBG col-sm-" + 6;
					var last = document.getElementById("last");
					last.className = "ChatBG col-sm-"+4;
					document.getElementById('chatInput').style.width = '45%';
					$scope.currentThread.push( message);
					
					
					
					$scope.currentReplys = [];
					$scope.isReplyShown = true;
					if($scope.chatMode == PUBLIC){
						var GET_MESSAGE_REPLIES = 2;
						//get replies of the reply
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":GET_MESSAGE_REPLIES, "message":$scope.currentThread[$scope.currentThread.length - 1]}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			$scope.replyUserInput = "@" + response.nickname + " ";
					    			if(response.data != undefined){
					    				$scope.currentReplys = response.data;
					    				
					    				
					    			}
					    			
					    			//sendMessage(response.pMessage,$scope.$parent.userIsReady,$scope.currentChannel);
					    		}
					    	});
					}else{
						var GET_Private_REPLIES = 4;
						$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
								{params: {"cmd":GET_Private_REPLIES, "message":$scope.currentThread[$scope.currentThread.length - 1]}})
					    .success(function (response) {
					    	var res = true;
					    		if(response.queryResult == res){
					    			//$scope.replyreply = response.pMessage;
					    			$scope.replyUserInput = "@" + response.nickname + " ";
					    			if(response.data != undefined){
					    				$scope.currentReplys = response.data;
					    				
					    				
					    			}
					    			
					    			//sendMessage(response.pMessage,$scope.$parent.userIsReady,$scope.currentChannel);
					    		}
					    	});
					}
					
					//$scope.$apply();
	            } 
				//upon clicking the back button - it returns to the previous thread
				$scope.returnToPreviousThread = function(){
					if($scope.currentThread.length <= 1){ //if it's the first parent then remove the reply window
						var chat = document.getElementById("chatChat");
						chat.className = "container fill ChatBG col-sm-" + 8;
						var last = document.getElementById("last");
						last.className = "ChatBG col-sm-"+2;
						document.getElementById('chatInput').style.width = '65%';
						$scope.isReplyShown = false;
						$scope.currentThread = [];
						return;
					}
					//else: pop the opened thread
					$scope.currentThread.pop();
					//return to the previous thread
					$scope.replyToThis($scope.currentThread.pop(),true);
				}
				
				//returns current opened channel
				$scope.getCurrentChannel = function (){
					return $scope.currentChannel;
				}
				
				$scope.replyToMessage = function(data){
					var chat = document.getElementById("chatChat");
					chat.className = "container fill ChatBG col-sm-" + 5;
				}
				
				//adds message to current messages
				$scope.updateMessages = function(msg){
					$scope.chatMessages.push(msg);
	        		chatConsole.scrollTop = chatConsole.scrollHeight;
					$scope.$apply();
				}
				//updates replies
				$scope.updateReplies = function(msg){
					$scope.currentReplys.push(msg);
					replyConsole.scrollTop = chatConsole.scrollHeight;
					$scope.$apply();
				}
				
				$scope.getCurrentMode = function(){
					return $scope.chatMode;
				}
				
				$scope.getPrivateChannels = function(){
					return $scope.privateChannels;
				}
				
				$scope.getPublicChannels = function(){
					return $scope.publicChannels;
				}
				$scope.getCurrentPrivateChannel = function(){
					return $scope.currentPrivateChannel;
				}
				//upon click on private chat
				$scope.openPrivateChat = function(chat){
					//zero the notifs
					$scope.currentThread = {};
					if($scope.isReplyShown){
						var chat = document.getElementById("chatChat");
						chat.className = "container fill ChatBG ng-hide col-sm-" + 8;
						var last = document.getElementById("last");
						last.className = "ChatBG col-sm-"+2;
						document.getElementById('chatInput').style.width = '65%';
						}
					zeroPrivateNotif(chat);
					
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":14,"username":$scope.$parent.userIsReady,
								"other":chat,"notifto":" ","notif":0}})
				    .success(function (response) {});
					
					$scope.chatMode = PRIVATE;
					//if i clicked on myself 
					if(chat == $scope.$parent.userIsReady){
						return;
					}
					/*if(checkIfExist(chat, $scope.privateChannels)){
						return;
					}*/
					$scope.currentPrivateChannel = chat;
					$scope.channelchannel = "#" + chat;
					$scope.currentChannel = "";
					$scope.isSearchShown = false;
					$scope.isChatShown = true;
					$scope.chatMessages = [];
					$scope.allMessages = [];
					var GET_PRIVATE_MESSAGES = 3;
					//bring chat messages
					$http.get("http://localhost:8080/webProject/ChatsManagerServlet",
							{params: {"cmd":GET_PRIVATE_MESSAGES,"username":$scope.$parent.userIsReady,
								"other":chat}})
				    .success(function (response) {
				    	var res = true;
				    		if(response.queryResult == res){
				    			$scope.chatMessages = response.data;
				    			if($scope.chatMessages == undefined){
				    				$scope.chatMessages = [];
				    			}
				    			else{
				    				$scope.allMessages = $scope.chatMessages;
				    				$scope.clicksclicks = 1;
				    				$scope.getTenMessages();
				    			}
				    			//appeandMessages($scope.chatMessages);
				    			
				    		}
				    		
				    	});
					
					//document.getElementById("chatConsole").innerHTML = "";
					help();
	        		chatConsole.scrollTop = chatConsole.scrollHeight;
				}
				function zeroPrivateNotif(chat){
					for(var i in $scope.privateChannels){
						if($scope.privateChannels[i].name == chat){
							$scope.privateChannels[i].notif = 0;
							$scope.privateChannels[i].mention = false;
							return;
						}
					}
				}
				$scope.getUser = function(){
					return $scope.$parent.userIsReady;
				}
				
				
				
				$scope.getcurrentThread = function(){
					return $scope.currentThread;
				}
				
				$scope.updateReplies = function(rply){
					$scope.currentReplys.push(rply);
					$scope.$apply();
				}
				//update notifs
				$scope.updatePrivateNotifications = function(pChat){
					for(var i in $scope.privateChannels){
						if($scope.privateChannels[i].name == pChat){
							$scope.privateChannels[i].notif++;
							$scope.$apply();
							break;
						}
							
					}
					
				}
				//update public notifs
				$scope.updatePublicNotifications = function(pChat){
					for(var i in $scope.publicChannels){
						if($scope.publicChannels[i].name == pChat){
							$scope.publicChannels[i].notif++;
							$scope.$apply();
							break;
						}
							
					}
					
				}
				
				$scope.getCurrentReplyThread = function(){
					return $scope.currentThread;
				}
				//when other user adds channel - the websocket will send me the  data through this func
				$scope.updateSiteChannels = function(channel,user,descreption){
					var channelObj ={};
					var channelInfo = {};
					var currentUsers = [];
					
					channelInfo.channelName = channel;
					channelInfo.description = descreption;
					currentUsers.push(user);
					
					channelObj.participants = currentUsers;
					channelObj.channel = channelInfo;
					
					//$scope.records.push(channelObj);
					$scope.tmpRecords.push(channelObj);
					$scope.$apply();
					
				}
				
				$scope.updatePrivateChannels = function(user){
					var pChannel = {};
					pChannel.name = user;
					pChannel.notif = 1;
					$scope.privateChannels.push(pChannel);
					
					$scope.$apply();
				}
				
				//when an old thread get renewed it's dragged to the bottom
				$scope.updateMessagesOrder = function(msg,parent){
					var tmpM = {};
					var tmpArray = [];
					for (var i = $scope.chatMessages.length - 1 ; i >= 0; i--) {
						if($scope.chatMessages[i] != undefined){
							if($scope.chatMessages[i].messageID == parent.messageID ){
								
								tmpM = $scope.chatMessages[i];
								delete $scope.chatMessages[i];
								break;
							}
							/*if($scope.chatMessages[i] != undefined){
								tmpArray.push($scope.chatMessages[i]);
							}*/
						}
					}
					tmpArray.push(tmpM);
					for (var i = $scope.chatMessages.length - 1 ; i >= 0; i--) {
						if($scope.chatMessages[i] != undefined){
							if($scope.chatMessages[i] != undefined){
								tmpArray.push($scope.chatMessages[i]);
							}
						}
					}
					tmpArray.reverse();
					$scope.chatMessages = tmpArray;
					$scope.$apply();
				}
				//upon mention
				$scope.alertMentionPublic = function(channel){
					for(var i in $scope.publicChannels){
						if($scope.publicChannels[i].name == channel){
							$scope.publicChannels[i].mention = true;							
							break;
						}
							
					}//add mention
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":10,"username":$scope.$parent.userIsReady,
								"channelName":channel}})
				    .success(function (response) {});
					$scope.$apply();
				}
				//mention private
				$scope.alertMention = function(pChat){
					for(var i in $scope.privateChannels){
						if($scope.privateChannels[i].name == pChat){
							$scope.privateChannels[i].mention = true;
							$scope.$apply();
							return;
						}
							
					}
				}
				//when user removes private chat was shared with another user - the chat disappears 
				//in both users
				$scope.removePrivate = function(user){
					var tmpArray = [];
					for (var i = $scope.privateChannels.length - 1 ; i >= 0; i--) {
						if($scope.privateChannels[i] != undefined){
							if($scope.privateChannels[i].name == user ){
								
								delete $scope.privateChannels[i];
							}
							if($scope.privateChannels[i] != undefined){
								tmpArray.push($scope.privateChannels[i]);
							}
						}
					}
					tmpArray.reverse();
					$scope.privateChannels = tmpArray;
					if($scope.currentPrivateChannel == user){
						$scope.currentChannel = "";
						$scope.isSearchShown = true;
						$scope.isChatShown = false;
						$scope.chatMessages = [];
					}
					$scope.$apply();
				}
				//removes notif from public channel
				$scope.removeNotifPublic = function (channel){
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":12,"username":$scope.$parent.userIsReady,
								"channelName":channel,"notif":0}})
				    .success(function (response) {});
				}
				//$scope.$apply();
				//removes notif from private
				$scope.removeNotifPrivate = function(msg){
					if(msg.username == $scope.$parent.userIsReady){
						return;
					}
					$http.get("http://localhost:8080/webProject/ChannelsManager",
							{params: {"cmd":14,"username":msg.username,
								"other":msg.other,"notifto":" ","notif":0}})
				    .success(function (response) {});
				}
				
			}]);
		
		function cconnect(user) { //connect to websocket as taught
        	
        	var wsUri = "ws://"+window.location.host+"/webProject/chat/Otalks/"+user;
        	console.log(wsUri,user);
            websocket = new WebSocket(wsUri);
            websocket.onopen = function(evt) {
            };
            websocket.onmessage = function(evt) {
            	var msg = JSON.parse(evt.data);
            	
            	notify(msg);
            };
            websocket.onerror = function(evt) {
            	notify('ERROR: ' + evt.data);
            };
            
            websocket.onclose = function(evt) {
            	websocket = null;
            };
            

            userInput.value = '';
            
            return websocket;
        }
		
		function help(){
            userInput.value = '';
		}
        //used upon regular message sent to public channel
        function sendMessage(msg,username,channelName) {
        	if (websocket != null){
        		var message = {
        				type: "public",
        				user: username,
        				channel: channelName,
        				messageObject: msg
        		};
           		 websocket.send(JSON.stringify(message));
            }
        	userInput.value = '';
        }
        //to private
        function sendMessagePrivate(msg,username,other) {
        	if (websocket != null){
        		var message = {
        				type: "private",
        				user: username,
        				other: other,
        				messageObject: msg
        		};
           		 websocket.send(JSON.stringify(message));
            }
        	userInput.value = '';
        }
        //when new channel created the ui updates for all
        function sendMessageNewChannel(channel,user,descreption){
        	if (websocket != null){
        		var message = {
        				type: "newChannel",
        				channel: channel,
        				channelDes: descreption,
        				user: user,
        		};
           		 websocket.send(JSON.stringify(message));
            }
        }
        //reply
        function sendMessageReply(msg,user,channel,parent){
        	if (websocket != null){
        		var message = {
        				type: "public",
        				user: user,
        				channel: channel,
        				messageObject: msg,
        				myParent : parent
        		};
           		 websocket.send(JSON.stringify(message));
            }
        	userInput.value = '';
        	
        }
        //private reply
        function sendMessagePrivateReply(msg,user,other,parent){
        	if (websocket != null){
        		var message = {
        				type: "private",
        				username: user,
        				otherP: other,
        				messageObject: msg,
        				myParent : parent
        		};
           		 websocket.send(JSON.stringify(message));
            }
        	userInput.value = '';
        	
        }
        //delete private chat
        function sendMessageDeletePrivate(user,other){
        	if (websocket != null){
        		var message = {
        				type: "deletePrivate",
        				username: user,
        				otherP: other,
        		};
           		 websocket.send(JSON.stringify(message));
            }
        	userInput.value = '';
        }
        
        /**the notify function get the message with additional data
         * new channel -
         * delete private channel - (for both)
         * private message/reply
         * public message/reply
         * 
         * */
        function notify(message) {
        	var mainPageCtrl = angular.element(document.getElementById('mainPageController'))
			.scope(); // in order to access the controller functions
        	
        	if(message.type == "newChannel"){
        		mainPageCtrl.updateSiteChannels(message.channel,message.user,message.channelDes);
        		return;
        	}
        	
        	//gets the data needed in order to do a successful mapping
        	var currentChannel = mainPageCtrl.getCurrentChannel();
        	var currentPrivateChannel = mainPageCtrl.getCurrentPrivateChannel();
        	var user = mainPageCtrl.getUser();
        	var mode = mainPageCtrl.getCurrentMode();
        	var myPublicChannels = mainPageCtrl.getPublicChannels();
        	var myPrivateChannels = mainPageCtrl.getPrivateChannels();
        	var thread = mainPageCtrl.getCurrentReplyThread();
        	var currentReplyThread = thread[thread.length - 1];
        	
        	var theMessage = message.messageObject;
        	
        	if(message.type == "deletePrivate"){
        		if(message.otherP == user ){
        			mainPageCtrl.removePrivate(message.username);
        		}
        		return;
        	}
        	
        	if(message.type == "private"){
        		if(theMessage.other == user || theMessage.username == user){
        			if(currentPrivateChannel == theMessage.username || theMessage.other == currentPrivateChannel){
        				if(theMessage.isReply){
        					//if the other user replied on the opened thread
        					if(theMessage.replyTo == currentReplyThread.messageID){
        						mainPageCtrl.updateReplies(theMessage);
        					}else{
        						//update ui (the message replied on must show in bottom) shift must be done
        					}
        					mainPageCtrl.updateMessagesOrder(theMessage,message.myParent);
        				}else{
        					mainPageCtrl.updateMessages(theMessage);
        				}
        				/***remove notif from server****/mainPageCtrl.removeNotifPrivate(theMessage);
        			}else{
        				//check if he exist in private chats and update private notification
        				if(checkIfExist(theMessage.username, myPrivateChannels)){
        					mainPageCtrl.updatePrivateNotifications(theMessage.username);
        				}
        				//if we haven't talk yet then add him to my private channels
        				else{
        					mainPageCtrl.updatePrivateChannels(theMessage.username);
        				}
        				var compr ="@"+user;
        				if(theMessage.content.substring(0,compr.length) == compr){
        					mainPageCtrl.alertMention(theMessage.username);
        				}
        				//check mention
        					
        			}
        		}else{
        			//none
        		}
        	}
        	//a public message received
        	else{
        		if(checkIfExist(theMessage.channelName, myPublicChannels)){
        			if(currentChannel == theMessage.channelName){
        				if(theMessage.isReply){
        					if(theMessage.replyTo == currentReplyThread.messageID){
        						mainPageCtrl.updateReplies(theMessage);
        					}else{
        						//update ui (the message replied on must show in bottom) shift must be done
        					}
        					mainPageCtrl.updateMessagesOrder(theMessage,message.myParent);
        				}else{
        					//update chat messages
        					mainPageCtrl.updateMessages(theMessage);
        				}
        				/***remove notif from server****/mainPageCtrl.removeNotifPublic(theMessage.channelName);
        			}else{
        				//update channels notifications
        				mainPageCtrl.updatePublicNotifications(theMessage.channelName);
        				//check mention
        				var compr ="@"+user;
        				var cont = theMessage.content.substring(0,compr.length);
        				if( cont == compr){
        					mainPageCtrl.alertMentionPublic(theMessage.channelName);
        				}
        			}
        		}else{
        			//none
        		}
        	}
     
        }
        
        /***
         * 
         * 
         * */
        
        function checkIfExist(x,arr){
        	for(var i in arr){
        		if(arr[i].name == x)
        			return true;
        	}
        }
        
