$(function(){
	$("#addReply").on('click', function(){
		window.location.href="gotoReply?replyId="+$("#id").val();
	});
	$("#back").on('click', function(){
		window.history.go(-1);
	});
})