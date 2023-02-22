const $SurveyList = document.querySelector("#SurveyList");
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
// const receivedPostId = location.href.split('?')[1];
var receivedPostId;

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

const fetchSurvey = () => {
    var requestOptions = {
        method: "GET",
        headers: {'x-access-token' : my_jwt,  'REFRESH-TOKEN' : my_refresh, }
    };

    fetch(
        `http://seolmunzip.shop:9000/users/userPost`,
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult.code);
            if(webResult.code == 1000) {
                webResult.result.map(item => SurveyListTemplate(item));
                if(webResult.result.length != 0){
                    $(".length_zero_txt").css("display","none");
                }
                slick();
            }
            if(webResult.code == 2002) {
                fetchTokenCheck();
                var requestOptions = {
                    method: "GET",
                    headers: {'x-access-token' : my_jwt,  'REFRESH-TOKEN' : my_refresh, }
                };
            
                fetch(
                    `http://seolmunzip.shop:9000/users/userPost`,
                    requestOptions
                )
                    .then((response) => response.json())
                    .then((webResult) => {
                        console.log(webResult.code);
                        if(webResult.code == 1000) {
                            webResult.result.map(item => SurveyListTemplate(item));
                            if(webResult.result.length != 0){
                                $(".length_zero_txt").css("display","none");
                            }
                            slick();
                        }
                    })
            }
            
        })
        .catch((error) => console.log("error", error));
}

fetchSurvey();

function SurveyListTemplate (data) {
    receivedPostId = data.postId;
    console.log(receivedPostId);
    const SurveyItem1 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../result.html?${receivedPostId}">${data.postTitle}</a>
        </div>
        <div class="two-container1">
            <div class="box">
                <span id="count1">문항 수 </span>
                <span id="count">${data.qcount}개</span>
            </div>
            <div class="box">
                <span id="point1">포인트</span>
                <span id="point">${data.point}P</span>
            </div>
            <div class="box">
                <span id="day">마감 기간</span>
                <span id="deadline">D-${data.dday}</span></div>
            </div>
            <div class="box">
                <span id="num">참여 인원</span>
                <span id="num">${data.postResultCount}명</span>
            </div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert(${receivedPostId});">설문삭제</button>
                                    <button id="modifyBtn" onClick="modify_alert(${receivedPostId});">설문수정</button></div>
        </div>
    </div>
    `;
    const SurveyItem2 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../result.html?${receivedPostId}">${data.postTitle}</a>
        </div>
        <div class="two-container1">
            <div class="box">
                <span id="count1">문항 수 </span>
                <span id="count">${data.qcount}개</span>
            </div>
            <div class="box">
                <span id="point1">포인트</span>
                <span id="point">${data.point}P</span>
            </div>
            <div class="box">
                <span id="day">마감 기간</span>
                <span id="deadline">CLOSED</span></div>
            </div>
            <div class="box">
                <span id="num">참여 인원</span>
                <span id="num">${data.postResultCount}명</span>
            </div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert(${receivedPostId});">설문삭제</button>
                                    <button id="modifyBtn" >수정불가</button></div>
            
        </div>
    </div>`;
    const SurveyItem3 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../result.html?${receivedPostId}">${data.postTitle}</a>
        </div>
        <div class="two-container1">
            <div class="box">
                <span id="count1">문항 수 </span>
                <span id="count">${data.qcount}개</span>
            </div>
            <div class="box">
                <span id="point1">포인트</span>
                <span id="point">${data.point}P</span>
            </div>
            <div class="box">
                <span id="day">마감 기간</span>
                <span id="deadline">D-DAY</span></div>
            </div>
            <div class="box">
                <span id="num">참여 인원</span>
                <span id="num">${data.postResultCount}명</span>
            </div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert(${receivedPostId});">설문삭제</button>
                                    <button id="modifyBtn" onClick="modify_alert(${receivedPostId});">설문수정</button></div>
        </div>
    </div>`;

    if(data.status =='CLOSED'){
        $SurveyList.insertAdjacentHTML('beforeend', SurveyItem2);
    }else if(data.dday == 0){
        $SurveyList.insertAdjacentHTML('beforeend', SurveyItem3);
    }else{
        $SurveyList.insertAdjacentHTML('beforeend', SurveyItem1);
    }
}

// 수정 alert
function modify_alert(postId) {
    Swal.fire({
        title: '수정하시겠습니까?',
        customClass: 'swal-wide',
        showCancelButton: true,
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예',
        cancelButtonText: '아니요'
    }).then((result) => {
        if (result.isConfirmed) {
            var link=`../modifyForm.html?${postId}`;
            location.href=link;
        }
    })
}

// 삭제 alert
function delete_alert(postId) {
    Swal.fire({
        title: '삭제하시겠습니까?',
        // text: "삭제하시겠습니까?",
        // icon: 'warning',
        customClass: 'swal-wide',
        showCancelButton: true,
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예',
        cancelButtonText: '아니요'
    }).then((result) => {
        if (result.isConfirmed) {
            deletePost(postId);
        }
    })
}

// 설문조사 삭제
function deletePost(postId) {
    const fetchDetail = () => {
        fetch(`http://seolmunzip.shop:9000/posts/${postId}/status`, {
            method: "PATCH",
            headers: {'x-access-token' : my_jwt, 'REFRESH-TOKEN' : my_refresh,}
        })
        .then((response) => response.json())
        .then((webResult) => {
                        
            console.log(webResult.code);
            if(webResult.code == 2002) {
                fetchTokenCheck();
                deletePost();
            }

            // alert("설문조사가 삭제되었습니다.");

            Swal.fire({
                title: '설문조사가 삭제되었습니다.',
                customClass: 'swal-wide',
                confirmButtonColor: '#4E7FF2',
                cancelButtonColor: '#DBDBDB',
                confirmButtonText: '예'
            }).then((result) => {
                if (result.isConfirmed) {
                    var link="../mySurvey.html";
                    location.href=link;
                }
            })

        })
        .catch((error) => console.log("error", error));
    }
    fetchDetail();
}

function slick(){
    $('#SurveyList').slick({
        slidesToShow: 2,
        slidesToScroll: 1,
        rows:3,
        infinite:false,
        prevArrow : $('.prev'), 
        nextArrow : $('.next'), 
    });
}
