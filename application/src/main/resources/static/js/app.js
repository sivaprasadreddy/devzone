
function deletePost(id)
{
    let yes = confirm("Are you sure to delete?");
    if (yes) {
        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");
        $.ajax ({
            url: '/posts/'+id,
            type: "DELETE",
            headers: {
              [header]: token
            },
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
        url: '/api/page-metadata?url='+url,
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

function initCategoriesAutoComplete(fieldSelector)
{
    $.ajax ({
      url: '/api/categories',
      type: "GET",
      dataType: "json",
      success: function(responseData){
        $(fieldSelector).selectize({
          maxItems: 1,
          valueField: 'name',
          labelField: 'name',
          searchField: 'name',
          options: responseData,
          create: true
        })
      }
  });
}
