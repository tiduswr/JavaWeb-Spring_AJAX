//Captura as metatags
$('#linkPromocao').on('change', function(){
    var url = $(this).val();
    if(url.length > 7){
        $.ajax({
            method: "POST",
            url: "/meta/info?url=" + url,
            cache: true,
            beforeSend: function(){
                $("#alert").removeClass("alert alert-danger").text("");
                $("#titulo").val("");
                $("#site").text("");
                $("#linkImagem").attr("src", "");
                $("#loader-img").addClass("loader")
            },
            success: function(data){
                $("#titulo").val(data.title);
                $("#site").text(data.site);
                $("#linkImagem").attr("src", data.image);
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
                $("#loader-img").removeClass("loader");
            }
        });
    }
})