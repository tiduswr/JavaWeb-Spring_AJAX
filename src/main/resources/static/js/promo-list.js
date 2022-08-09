var pageNumber = 0;

function loadByScrollBar(pageNumber){
    $.ajax({
        method: "GET",
        url: "/promocao/list/ajax",
        data: {
            page : pageNumber
        },
        success: function(response){
            $(".main-list").fadeIn(250, function(){
                $(this).append(response)
            });
        }
    });
}

//Efeito infinite Scroll
$(window).scroll(function(){
    var scrollTop = $(this).scrollTop();
    var conteudo = ($(document).height() - $(window).height()) - 1;
    console.log("scrollTop: " + scrollTop + " | conteudo: " + conteudo);
    if(scrollTop >= conteudo){
        pageNumber++;
        setTimeout(function(){loadByScrollBar(pageNumber)}, 200);
    }
})