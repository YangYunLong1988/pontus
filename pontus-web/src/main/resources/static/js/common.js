// JavaScript Document


$(function(){
	/*tab切换*/	
		 function tabs(tabTit,on,tabCon){
			$(tabCon).each(function(){
			$(this).children().eq(0).show();
			});
			$(tabTit).each(function(){
				$(this).children().eq(0).addClass(on);
			});
			$(tabTit).children().click(function(event){
				event.stopPropagation();
				$(this).addClass(on).siblings().removeClass(on);
				var index = $(tabTit).children().index(this);
				$(tabCon).children().eq(index).show().siblings().hide();
			});
		}
	  tabs(".tab-head","active",".tab-content");
	  
		$("#layertwoTab ul.tab-title02 li").click(function(){
			$(this).addClass('active').siblings().removeClass('active');
			var index = $(this).index();
			$("#layertwoTab div.tab").eq(index).show().siblings('.tab').hide();
		});
	  
	  $("span.pitchOn").click(function(){
		  if($(this).hasClass('on')){
			  $(this).removeClass('on');
			  }else{
			$(this).addClass('on').siblings().removeClass('on');  
				  }
		  });
	  $("a.close,.ensure a.guapaiBtn02").click(function(){
		  $(".mask").fadeOut(800);
		  $(".adduploadFile").fadeOut(800);
		  });
	  $("a.checkuploadFile").click(function(){
		  $(".mask").fadeIn(800);
		  $("#uploadFile").fadeIn(800);
		  });
	 $("a.checkunuploadFile").click(function(){
		  $(".mask").fadeIn(800);
		  $("#seekFile").fadeIn(800);
		  });
	$("a#guapaiOrder").click(function(){
		  $(".mask").fadeIn(800);
		  $("#uploadFile").fadeIn(800);
		  });   
		  
	/*附件查看*/		  
	
	var index = 0;
	var preBtn = $("#preBtn");
	var nextBtn = $("#nextBtn");	  
	var uploadBtn = $("#uploadBtn");
	var fullscreenBtn = $("#fullscreenBtn");
	var img = $('.file-img').find('img');
	var len = img.length;	  
	
	var showimg = function(i){
		img.stop(true,true).fadeOut().eq(i).stop(true,true).fadeIn();
		}
	showimg(0);
	preBtn.addClass('color99');
	nextBtn.click(function(){
		if(index >=len-1){
			index =len-1;
			showimg(index);
			nextBtn.addClass('color99');
			}else{	
					showimg(++index);
					preBtn.removeClass('color99');
				}
		});
			  
	preBtn.click(function(){
		if(index <=0){
			index = 0;
			preBtn.addClass('color99');
			showimg(index);
			}else{
				nextBtn.removeClass('color99');
				showimg(--index);
				}
		});
	
	$("#uploadBtn").click(function(){
		$("#seekFile").fadeOut();
		$("#uploadFile").fadeIn();
		});
	$("#fullscreenBtn").click(function(){
		$("#seekFile").addClass('adduploadFile02');
		});	  
	
	$("#getCodeMsg").click(function(){
		$(this).removeClass('codeBd01').addClass('codeBd02').siblings('span').text('短信验证码已发至139****1122号码上');
		});
	

	/*排序*/	
	$(".asset_table a.rank").click(function(){
		if($(this).find('i.default')){
			$(this).find('i').addClass("button-up");
			};
		});
	$(".asset_table a.rank").click(function(){
		if($(this).find('i.button-up')){
			$(this).find('i').addClass("button-down");
			};
		});
		
	$(".asset_table a.rank").click(function(){
		if($(this).find('i.button-down')){
			$(this).find('i').addClass("default");
			};
		});

	/*添加银行卡*/	

	$(".bankInfo a.abank").click(function(){
		if($(this).hasClass('active')){
			$(this).removeClass('active');
			}else{
				$(this).addClass('active').parent('li').siblings().find('a.abank').removeClass('active');
				}
		
		});

	$(".bankInfo a.addbank").click(function(){
		$(".returnBank").hide();
		$(".amendPw").show();
		});
	$("a#addBtn").click(function(){
		$(".amendPw").hide();
		$(".addsucces").show();
		});
	$("#blank-list li:gt(9)").hide();
	$("#blank-list li").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		});
	$("#blank-list a.more-bank").click(function(){
		if($("#blank-list li:gt(9)").is(':hidden')){
			$("#blank-list li:gt(9)").show();
			$(this).html('收起<i></i>');
			}else{
			$("#blank-list li:gt(9)").hide();
			$(this).html('更多充值银行<i></i>');	
				}
		
		});
	$("#fogetpwBtn").click(function(){
		$("#changeFtpw").hide();
		$("#fogetPw").show();
		});

	
	});
	
	
	
	
	