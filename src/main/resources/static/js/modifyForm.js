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
        "postId" : postId,
        "title" : updateTitle,
        "content" : updateContent,
        "deadline" : updateDeadline,
        "postUserId" : 1
    }

    console.log(JSON.stringify(modifyItem));

    fetch(`http://seolmunzip.shop:9000/posts` , {
        method: "PATCH",
        headers: {
            'X-ACCESS-TOKEN' : my_jwt, 
            'Content-Type': 'application/json'            
        },
        body: JSON.stringify(modifyItem)
    })

    .then((response) => response.json())
    .then((response2) => {
        console.log(response2.code);
        if (response2.code != 1000) {
            alert(response2.message);
        } else if (response2.code == 1000) {
            Swal.fire({
                title: '설문조사가 수정되었습니다.',
                customClass: 'swal-wide',
                confirmButtonColor: '#4E7FF2',
                cancelButtonColor: '#DBDBDB',
                confirmButtonText: '예'
            }).then((result) => {
                if (result.isConfirmed) {
                    gotoDetail(); 
                    // 잘 가는지 확인. 함수 속 href 링크가 헷갈림.
                }
            })
        }

    })
    .catch((error) => console.log("error", error))

}