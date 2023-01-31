var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');

const postId = location.href.split('?')[1];
console.log(postId);

var updateTitle;
var updateContent;
var updateDeadline;

function gotoDetail() {
    var link=`../detailPage.html?${postId}`;
    location.href=link;
}

function modifyContent() {
    updateTitle = document.getElementById("inputTitle").value;
    updateContent = document.getElementById("inputExplain").value;
    updateDeadline = document.getElementById("inputDate").value;
    fetchModifyForm();
}

function fetchModifyForm() {
    const modifyItem = {
        // "userId" : 1,
        // "content" : newContent
        "postId" : postId,
        "title" : updateTitle,
        "content" : updateContent,
        "deadline" : updateDeadline,
        "postUserId" : 1
    }

    console.log(JSON.stringify(modifyItem));

    fetch(`http://seolmunzip.shop:9000/posts/${postId}` , {
        method: "PATCH",
        headers: {
            'X-ACCESS-TOKEN' : my_jwt, 
            'Content-Type': 'application/json'            
        },
        body: JSON.stringify(modifyItem)
    })

    .then((response) => response.json())
    .then((response2) => {
        // if (response2.code == 2022) {
        //     alert(response2.message);
        //     gotoMain();
        // } else if (response2.code == 2022) {
        //     alert(response2.message);
        //     gotoMain();
        // } else if (response2.code == 3020) {
        //     alert(response2.message);
        //     gotoMain();
        // } else {
        //     gotoMysurvey();
        // }

        Swal.fire({
            title: '설문조사가 수정되었습니다.',
            customClass: 'swal-wide',
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예'
        }).then((result) => {
            if (result.isConfirmed) {
                gotoDetail(); 
            }
        })
        
        console.log(response2.result);
    })
    .catch((error) => console.log("error", error))

}