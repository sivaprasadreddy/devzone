
function deleteLink(id)
{
    let yes = confirm("Are you sure to delete?");
    if (yes) {
        $.ajax ({
            url: '/links/'+id,
            type: "DELETE",
            success: function(responseData, status){
                window.location = '/'
            }
        });
    }

}

function getPageMetadata(formId)
{
    let url = $("#"+formId+" input[name=url]").val();
    if(url === '') return;
    $.ajax ({
        url: '/page-metadata?url='+url,
        type: "GET",
        dataType: "json",
        success: function(responseData, status){
            //console.log(responseData);
            if(responseData.title !== '') {
                $("#"+formId+" input[name=title]").val(responseData.title)
            }
        }
    });
}
