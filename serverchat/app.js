var express = require("express");
var app = express();
var server = require("http").createServer(app);
var io = require("socket.io").listen(server);
var fs = require("fs");
server.listen(process.env.PORT || 3000);

var mangUsername = [];

io.sockets.on('connection', function (socket) {
	
  	console.log("Co nguoi connect ne");
  
  	socket.on('client-gui-username', function (data) {

  		console.log('Client vừa đăng ký username = '+data);

  		var ketquadk = false;
  		if(mangUsername.indexOf(data)>-1){
  			ketquadk = false;
  		}
  		else{
  			mangUsername.push(data);
  			socket.un = data;
  			ketquadk = true;
  		}
  	
		  io.sockets.emit('server-gui-username', { dsun: mangUsername });
  	});  

    socket.on('client-gui-tin-chat',function(noidungchat){
      console.log(socket.un +": "+noidungchat);

      var infocontentchat = [socket.un,noidungchat]

      io.sockets.emit('server-gui-tin-chat',{datachat: infocontentchat});

    });

    socket.on('client-gui-anh-chat',function(mangbyte){

      if(mangbyte!=null)
      {
        console.log(mangbyte);
        var infohinh = [socket.un, mangbyte];

        io.sockets.emit('server-gui-anh-chat',{datahinh: infohinh});
      }
    });
});