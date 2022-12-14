var pageNumber = 0;

function loadByScrollBar(pageNumber){
    var site = $("#autocomplete-input").val();
    $.ajax({
        method: "GET",
        url: "/promocao/list/ajax",
        data: {
            page : pageNumber,
            site : site
        },
        beforeSend: function(){
            $("#loader-img").show();
        },
        success: function(response){
            if(response.length > 150){
                $(".main-list").fadeIn(250, function(){
                    $(this).append(response);
                });
            }else{
                $("#fim-btn").show();
                $("#loader-img").removeClass("loader");
            }
        },
        error: function(xhr){
            alert("Ops, ocorreu um erro: " + xhr.status + " - " + xhr.statusText);
        },
        complete: function(){
            $("#loader-img").hide();
        }
    });
}

$(document).ready(function(){
    //Efeito infinite Scroll
    $("#loader-img").hide();
    $("#fim-btn").hide();
});

//Efeito infinite Scroll
$(window).scroll(function(){
    var scrollTop = Math.ceil($(this).scrollTop());
    var conteudo = (Math.ceil($(document).height()) - Math.ceil($(window).height()));
    if(scrollTop >= conteudo){
        pageNumber++;
        setTimeout(function(){loadByScrollBar(pageNumber)}, 200);
    }
})

//Adicionar likes
$(document).on("click", "button[id*='likes-btn-']", function(){
    var id = $(this).attr("id").split("-")[2];

    $.ajax({
        method: "POST",
        url: "/promocao/like/" + id,
        success: function(response){
            $("#likes-count-" + id).text(response);
        },
        error: function(xhr){
            alert("Ops, ocorreu um erro: " + xhr.status + " - " + xhr.statusText);
        }
    });

});

//Autocomplete
$("#autocomplete-input").autocomplete({
    source: function(request, response){
        $.ajax({
            method: "GET",
            url: "/promocao/site",
            data: {
                termo: request.term
            },
            success: function(result){
                response(result);
            }
        });
    }
});

$("#autocomplete-submit").on("click", function(){
    var site = $("#autocomplete-input").val();
    $.ajax({
        method: "GET",
        url: "/promocao/site/list",
        data: {
            site : site
        },
        beforeSend: function(){
            pageNumber = 0;
            $("#fim-btn").hide();
            $(".main-list").fadeOut(400, function(){
                $(this).empty();
            });
        },
        success: function(response){
            $(".main-list").fadeIn(200, function(){
                $(this).append(response);
            });
        },
        error: function(xhr){
            alert("Ops, ocorreu um erro: " + xhr.status + " - " + xhr.statusText);
        }
    });
});