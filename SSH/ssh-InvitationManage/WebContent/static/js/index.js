$(function(){
	var pageIndex = 1;
	var totalPage = 0;
	$("#search").on('click', function(){
		search();
	});
	
	function search(){
		var data  = {
				title : $("#title").val(),
				pageIndex : pageIndex
			};
			$.ajax({
				type:'post',
				url : 'search',
				data : data,
				dataType : 'json',
				success : function(resp){
					totalPage = resp.totalPage;
					if(totalPage > 0){
						$(".pageBox").addClass("active");
						$("#pageIndex").text(pageIndex);
						$("#totalPage").text(totalPage);
					} else {
						$(".pageBox").removeClass("active");
					}
					var invitations = resp.invitations;
					var html = '';
					for(var i=0; i<invitations.length; i++){
						html += "<tr>";
						html += "<td>" + invitations[i].title + "</td>";
						html += "<td>" + invitations[i].summary + "</td>";
						html += "<td>" + invitations[i].author + "</td>";
						html += "<td>" + invitations[i].createDate + "</td>";
						html += "<td><a href='viewReply?viewId="+invitations[i].id+"'>查看回复</a><a href='#' data-id='"+invitations[i].id+"' class='delete'>删除</a></td>";
						html +="</tr>";
					}
					$("#dataBox").html(html);
				}
			});
	}
	
	$("#dataBox").on('click', '.delete', function(){
		var select = confirm("确认删除该条发帖及相关回复？");
		if(!select) return;
		var data = {
			deleteId : $(this).attr('data-id')
		};
		$.ajax({
			type:'post',
			url : 'delete',
			data : data,
			dataType:'json',
			success:function(resp){
				if(resp && resp == true){
					search();
				}
			}
		});
	});
	
	$("#first").on('click', function(){
		if(pageIndex  == 1) return;
		pageIndex = 1;
		search();
	});
	$("#previous").on('click', function(){
		if(pageIndex  == 1) return;
		pageIndex -= 1;
		search();
	});
	$("#next").on('click', function(){
		if(pageIndex  == totalPage) return;
		pageIndex += 1;
		search();
	});
	$("#last").on('click', function(){
		if(pageIndex  == totalPage) return;
		pageIndex = totalPage;
		search();
	});
	
	search();
});