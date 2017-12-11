$(function(){
	$("#submit").on('click', function(){
		var reply = $("#reply").val();
		if(reply == undefined || reply == ''){
			alert("回复内容不能为空");
			return;
		}
		var id = $("#replyId").val();
		var data = {
		    invid : id,
			content : reply,
			author : $("#nickname").val()
		}
		$.ajax({
			url : 'addReply',
			type: 'post',
			data : data,
			dataType : 'json',
			success : function(resp){
				if(resp && resp == true){
					alert('添加回复成功');
				} else {
					alert('添加回复失败');
				}
				window.location.href="viewReply?viewId="+id;
			}
		});
	});
});