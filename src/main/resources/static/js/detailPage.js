const $SurveyDetail = document.querySelector("#detailMain");
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
var myPost;
var like;

const receivedPostId = location.href.split('?')[1];
console.log(receivedPostId);

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

const fetchDetail = () => {
    fetch(`http://seolmunzip.shop:9000/posts/content/${receivedPostId}`, {
        method: "GET",
        headers: {'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, }
    })
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult.code);
            console.log(webResult.message);
            if(webResult.code == 1000) {
                SurveyDetailTemplate(webResult.result);
            }
            if(webResult.code == 2002) {

                fetchTokenCheck();
                // var requestOptions = {
                //     method: "Get",
                //     headers: {'REFRESH-TOKEN' : my_refresh, }
                // };
            
                // fetch(
                //     "http://seolmunzip.shop:9000/auth/refresh",
                //     requestOptions
                // )
                //     .then((response) => response.json())
                //     .then((webResult) => {
                //         console.log(webResult.code);
                //         localStorage.removeItem('x-access-token');
                //         localStorage.setItem('x-access-token', webResult.result);
                //         my_jwt = localStorage.getItem('x-access-token');
                //     })
                //     .catch((error) => console.log("error", error));

                                // // 새로고침
                                // if(localStorage.getItem('flag')) {
                                //     localStorage.removeItem('flag');
                                // } else {
                                //     localStorage.setItem('flag', "flagValue");
                                //     location.reload();
                                // }

                // 재호출
                fetch(`http://seolmunzip.shop:9000/posts/content/${receivedPostId}`, {
                    method: "GET",
                    headers: {'X-ACCESS-TOKEN' : my_jwt, 'REFRESH-TOKEN' : my_refresh, }
                })
                .then((response) => response.json())
                .then((webResult) => {
                    console.log(webResult.code);
                    console.log(webResult.message);
                    if(webResult.code == 1000) {
                        SurveyDetailTemplate(webResult.result);
                    }
                })

            }
        })
        .catch((error) => console.log("error", error));
}
fetchDetail();

function SurveyDetailTemplate (data) {

    // 내 게시물 아니고, 참여 이미 했음
    const SurveyDetailItem_participated_like = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart2.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-${data.dday}</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="location.href = '../templates/answer.html?${receivedPostId}'">나의&nbsp;&nbsp;답변&nbsp;&nbsp;확인</button>
        </div> 
    `;
    const SurveyDetailItem_participated_nolike = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-${data.dday}</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="location.href = '../templates/answer.html?${receivedPostId}'">나의&nbsp;&nbsp;답변&nbsp;&nbsp;확인</button>
        </div> 
    `;

    const SurveyDetailItem_closed_myPost = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert();">설문삭제</button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; CLOSED</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="closed_alert();">내가&nbsp;&nbsp;만든&nbsp;&nbsp;설문</button>
        </div> 
    `;
    const SurveyDetailItem_closed_like = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart2.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; CLOSED</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="closed_alert();">마감된&nbsp;&nbsp;설문</button>
        </div> 
    `;
    const SurveyDetailItem_closed_nolike = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp;CLOSED</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="closed_alert();">마감된&nbsp;&nbsp;설문</button>
        </div> 
    `;
    const SurveyDetailItem_dday_myPost = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert();">설문삭제</button>
                                    <button id="modifyBtn" onClick="modify_alert(${receivedPostId});">설문수정</button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-DAY</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="mypost_alert()">내가&nbsp;&nbsp;만든&nbsp;&nbsp;설문</button>
        </div> 
    `;
    const SurveyDetailItem_dday_like = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart2.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-DAY</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn" onClick="location.href='../templates/joinForm.html?${receivedPostId}'">설문&nbsp;&nbsp;참여</button>
        </div> 
    `;
    const SurveyDetailItem_dday_nolike = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp;D-DAY</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn" onClick="location.href='../templates/joinForm.html?${receivedPostId}'">설문&nbsp;&nbsp;참여</button>
        </div> 
    `;

    const SurveyDetailItem_d_myPost = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="deleteBtn" onClick="delete_alert();">설문삭제</button>
                                    <button id="modifyBtn" onClick="modify_alert(${receivedPostId});">설문수정</button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-${data.dday}</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn_gray" onClick="mypost_alert()">내가&nbsp;&nbsp;만든&nbsp;&nbsp;설문</button>
        </div> 
    `;
    const SurveyDetailItem_d_like = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart2.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp; D-${data.dday}</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn" onClick="location.href='../templates/joinForm.html?${receivedPostId}'">설문&nbsp;&nbsp;참여</button>
        </div> 
    `;
    const SurveyDetailItem_d_nolike = `
        <div id="mainTop">
        <div class="flex-container1">
            <div class="flex-item1"><span id="detailTitle"> ${data.title} </span></div>
            <div class="flex-item1"><button id="heart"><img class="heartImg" src="../static/image/Heart.png" width="40%" onclick="heart();"/></button></div>
        </div>

        <div class="flex-container2">
            <div class="flex-item2"><span id="items">${data.qcount}개의 항목&nbsp;&nbsp;ㅣ</span></div>
            <div class="flex-item2"><span id="deadline">&nbsp;&nbsp;D-${data.dday}</span></div>
        </div>
        </div>

        <div id="mainBottom"> ${data.content} </div>

        <div class="join">
            <button id="joinBtn" onClick="location.href='../templates/joinForm.html?${receivedPostId}'">설문&nbsp;&nbsp;참여</button>
        </div> 
    `

    
    if(data.status == 'CLOSED'){
        if(data.myPost == true){ // 설문 삭제
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_closed_myPost);
        }else if(data.myPost == false && data.like == true ){ // 찬하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_closed_like);
        }else if(data.myPost == false && data.like == false){ // 빈하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_closed_nolike);
        }
    }
    else if(data.participation == true && data.myPost == false) { // 이미 참여 (내 게시물 아님)
        if(data.like == true) { // 찬하트
             $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_participated_like);
        } else if(data.like == false) { // 빈하트
             $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_participated_nolike);
        }
    }
    else if(data.dday == 0){
        if(data.myPost == true && data.status == 'ACTIVE'){ // 설문 삭제
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_dday_myPost);
        }else if(data.myPost == false && data.like == true ){ // 찬하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_dday_like);
        }else if(data.myPost == false && data.like == false){ // 빈하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_dday_nolike);
        }
    }
    else{
        if(data.myPost == true && data.status == 'ACTIVE'){ // 설문 삭제
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_d_myPost);
        }else if(data.myPost == false && data.like == true ){ // 찬하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_d_like);
        }else if(data.myPost == false && data.like == false){ // 빈하트
            $SurveyDetail.insertAdjacentHTML('beforeend', SurveyDetailItem_d_nolike);
        }
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
            var link=`../templates/modifyForm.html?${postId}`;
            location.href=link;
        }
    })
}

// 삭제 alert
function delete_alert() {
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
            deletePost();
        }
    })
}

// 설문조사 삭제
function deletePost() {
    console.log("delete");

    const fetchDetail = () => {
        fetch(`http://seolmunzip.shop:9000/posts/${receivedPostId}/status`, {
            method: "PATCH",
            headers: {'x-access-token' : my_jwt,}
        })
        .then((response) => response.json())
        .then((webResult) => {
            Swal.fire({
                title: '설문조사가 삭제되었습니다.',
                customClass: 'swal-wide',
                confirmButtonColor: '#4E7FF2',
                cancelButtonColor: '#DBDBDB',
                confirmButtonText: '예'
            }).then((result) => {
                if (result.isConfirmed) {
                    var link="../templates/mySurvey.html";
                    location.href=link;
                }
            })
        })
        .catch((error) => console.log("error", error));
    }
    
    fetchDetail();
}

// 하트 처리
function heart(){
    const $heartImg = document.querySelector(".heartImg");
    const $heartImgCheck = document.querySelector(".heartImg").getAttribute( 'src' );

    if($heartImgCheck == "../static/image/Heart.png" || like == false){
        like = true;
        $heartImg.setAttribute('src',"../static/image/Heart2.png"); // 찬 하트
        interested_item_add();
        
    }else if($heartImgCheck == "../static/image/Heart2.png" || like == true){
        like = false;
        $heartImg.setAttribute('src',"../static/image/Heart.png"); // 빈 하트
        interested_item_delete();
    }
}

// 관심있는 설문조사 등록
function interested_item_add(){
    fetch(`http://seolmunzip.shop:9000/posts/interest/${receivedPostId}`, {
        method: "POST",
        headers: {'x-access-token' : my_jwt,}
    })
    .then((response) => response.json())
    .catch((error) => console.log("error", error))
}

// 관심있는 설문조사 삭제
function interested_item_delete(){
    fetch(`http://seolmunzip.shop:9000/posts/interest/${receivedPostId}`, {
        method: "DELETE",
        headers: {'x-access-token' : my_jwt,}
    })

    .then((response) => response.json())
    .catch((error) => console.log("error", error))
}

function closed_alert(){
    alert("이미 마감된 설문조사입니다.");
}

function participated_alert(){
    alert("이미 참여한 설문조사입니다.");
}

function mypost_alert(){
    alert("내가 만든 설문조사입니다.");
}