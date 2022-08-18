//Datatable var
var table = null;

$(document).ready(function () {
    moment.locale("pt-br");
    table = $("#table-server").DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        lengthMenu: [10, 15, 20, 25],
        ajax: {
            url: "/promocao/datatables/server",
            data: "data"
        },
        columns: [
            {data: "id"},
            {data: "titulo"},
            {data: "site"},
            {data: "linkPromocao"},
            {data: "descricao"},
            {data: "linkImagem"},
            {data: "preco", render: $.fn.dataTable.render.number("-", ",", 2, "R$")},
            {data: "likes"},
            {data: "dtCadastro", render: function(dtCadastro){
                return moment(dtCadastro).format("LL");
            }},
            {data: "categoria.titulo"}
        ],
        dom: "Bfrtip",
        buttons: [
            {
                text: "Editar",
                attr: {
                    id: "btn-editar",
                    type: "button"
                },
                enabled: false
            },
            {
                text: "Excluir",
                attr: {
                    id: "btn-excluir",
                    type: "button"
                },
                enabled: false
            }
        ]
    });    
});

//Desabilita botões ao ordenar
$("#table-server thead").on("click", "tr", function () {
    table.buttons().disable();
});

//Habilita/Desabilita botões ao selecionar linhas
$("body").on("click", "#table-server tbody tr", function(){
    if($(this).hasClass("selected")){
        $(this).removeClass("selected");
        table.buttons().disable();
    }else{
        $("tr.selected").removeClass("selected");
        $(this).addClass("selected");
        table.buttons().enable();
    }
});

//Ação do botão editar
$("body").on("click", "#btn-editar", function(){
    if(isSelectedRow()){
        var id = getPromoId();
        $("#modal-form").modal("show");
    }
});

//Ação do botão editar
$("body").on("click", "#btn-excluir", function(){
    if(isSelectedRow()){
        var id = getPromoId();
        $("#modal-delete").modal("show");
    }
});


//################################## FUNCTIONS

function getPromoId(){
    return table.row(table.$("tr.selected")).data().id;
}

function isSelectedRow(){
    var trow = table.row(table.$("tr.selected"));
    return trow.data() !== undefined;
}