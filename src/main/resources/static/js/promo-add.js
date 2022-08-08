function resetPromoFormFieldsOnSubmit(){
    $("#form-add-promo").each(function(){
        this.reset();
    });
    $("#linkImagem").attr("src", "/images/promo-dark.png");
    $("#site").text("")
}
function resetPromoFormFieldsOnChange(){
    $("#alert").removeClass("alert alert-danger alert-success").text("");
    $("#titulo").val("");
    $("#site").text("");
    $("#linkImagem").attr("src", "");
}
function promoAddPageLoaderHide(){
    $("#loader-form").fadeOut(800, function(){
        $("#form-add-promo").fadeIn(250);
        $("#loader-form").removeClass("loader");
    });
}
function promoAddLoaderShow(){
    $("#form-add-promo").hide();
    $("#loader-form").addClass("loader").show();
}
function loadPromoLinkMetaInfo(data){
    $("#titulo").val(data.title);
    $("#site").text(data.site);
    $("#linkImagem").attr("src", data.image);
}
function configPromoAddErrorFields(xhr){
    var errors = $.parseJSON(xhr.responseText);
    $.each(errors, function(key, val){
        $("#"+key).addClass("is-invalid");
        $("#error-"+key)
            .addClass("invalid-feedback")
            .append("<span class='error-span'>" + val + "</span>");
    })
}
function cleanFieldErrorsPromoAddPage(){
    //Removendo mensagens de erro
    $("span").closest(".error-span").remove();

    //Remover as Bordas vermelhas
    $("#categoria").removeClass("is-invalid");
    $("#preco").removeClass("is-invalid");
    $("#linkPromocao").removeClass("is-invalid");
    $("#titulo").removeClass("is-invalid");
}



//Faz submit do formulario
$("#form-add-promo").submit(function(evt){
    //Bloq submit default operation
    evt.preventDefault();
    
    var promo = {};

    promo.linkPromocao = $("#linkPromocao").val();
    promo.descricao = $("#descricao").val();
    promo.preco = $("#preco").val();
    promo.titulo = $("#titulo").val();
    promo.categoria = $("#categoria").val();
    promo.linkImagem = $("#linkImagem").attr("src");
    promo.site = $("#site").text();

    console.log("promo > ", promo);

    $.ajax({

        method: "POST",
        url: "/promocao/save",
        data: promo,
        beforeSend: function () {
            cleanFieldErrorsPromoAddPage();
            promoAddLoaderShow();
        },
        success: function(){
            resetPromoFormFieldsOnSubmit();
            $("#alert")
                .removeClass("alert alert-danger")
                .addClass("alert alert-success")
                .text("OK! Promoção cadastrada com Sucesso!");
        },
        error: function(xhr){
            console.log("> error: ", xhr.responseText);
            $("#alert")
                .removeClass("alert alert-success")
                .addClass("alert alert-danger")
                .text("Não foi possivel salvar esta promoção!");
        },
        statusCode: {
            422: function(xhr){
                configPromoAddErrorFields(xhr);
            }
        },
        complete: function(){
            promoAddPageLoaderHide();
        }
    })

})

//Captura as metatags
$('#linkPromocao').on('change', function(){
    var url = $(this).val();
    if(url.length > 7){
        $.ajax({
            method: "POST",
            url: "/meta/info?url=" + url,
            cache: true,
            beforeSend: function(){
                resetPromoFormFieldsOnChange();
                //Habilita loader da imagem
                $("#loader-img").addClass("loader");
            },
            success: function(data){
                loadPromoLinkMetaInfo(data);
            },
            statusCode: {
                404: function(){
                    $("#alert").addClass("alert alert-danger").text("Nenhuma informação encontrada nessa URL!");
                    $("#linkImagem").attr("src", "/images/promo-dark.png");
                }
            },
            error: function(){
                $("#alert").addClass("alert alert-danger").text("Ops... Algo deu errado, tente mais tarde!");
                $("#linkImagem").attr("src", "/images/promo-dark.png");
            },
            complete: function(){
                //Desabilita loader da imagem
                $("#loader-img").removeClass("loader");
            }
        });
    }
})