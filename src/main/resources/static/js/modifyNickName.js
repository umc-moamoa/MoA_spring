var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
var updateNickName;

const fetchTokenCheck = () => {
    var requestOptions = {
        method: "Get",
        headers: {'REFRESH-TOKEN' : my_refresh, }
    };

    fetch(
        "http://seolmunzip.shop:9000/auth/refresh",
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult.code);
            localStorage.removeItem('x-access-token');
            localStorage.setItem('x-access-token', webResult.result);
            my_jwt = localStorage.getItem('x-access-token');
        })
        .catch((error) => console.log("error", error));
}

function modifyContent() {
    updateNickName = document.getElementById("inputTitle").value;
    fetchModifyForm();
}

function fetchModifyForm() {
    const modifyItem = {
        "newNickName" : updateNickName
    }

    console.log(JSON.stringify(modifyItem));

    fetch(`http://seolmunzip.shop:9000/users/nick` , {
        method: "PATCH",
        headers: {
            'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh,
            'Content-Type': 'application/json'            
        },
        body: JSON.stringify(modifyItem)
    })

    .then((response) => response.json())
    .then((response2) => {
        console.log(response2);
        console.log(response2.result);

        if(response2.code == 1000) {
            Swal.fire({
                title: '닉네임이 변경되었습니다.',
                customClass: 'swal-wide',
                confirmButtonColor: '#4E7FF2',
                cancelButtonColor: '#DBDBDB',
                confirmButtonText: '예'
            }).then((result) => {
                if (result.isConfirmed) {
                    gotoMysurvey();
                }
            })

        } else if(webResult.code == 2002) {
            fetchTokenCheck();
            const modifyItem = {
                "newNickName" : updateNickName
            }
        
            console.log(JSON.stringify(modifyItem));
        
            fetch(`http://seolmunzip.shop:9000/users/nick` , {
                method: "PATCH",
                headers: {
                    'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh,
                    'Content-Type': 'application/json'            
                },
                body: JSON.stringify(modifyItem)
            })
        
            .then((response) => response.json())
            .then((response2) => {
                console.log(response2);
                console.log(response2.result);
        
                if(response2.code == 1000) {
                    Swal.fire({
                        title: '닉네임이 변경되었습니다.',
                        customClass: 'swal-wide',
                        confirmButtonColor: '#4E7FF2',
                        cancelButtonColor: '#DBDBDB',
                        confirmButtonText: '예'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            gotoMysurvey();
                        }
                    })
        
                }
            })
        }

    })
    .catch((error) => console.log("error", error))

}

function gotoMysurvey() {
    var link=`../myPage.html`;
    location.href=link;
}