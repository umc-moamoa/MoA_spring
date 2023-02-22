// jwt 관리하는 파일


// 변수 선언
//var my_access = localStorage.getItem('x-access-token');
//var my_refreshToken = localStorage.getItem('x-refresh-token');

var access_token = localStorage.getItem('x-access-token');
var refresh_token = localStorage.getItem('x-refresh-token');
const link_login = document.querySelector(".link_login");
const link_logout = document.querySelector(".link_logout");

// 로그인 후 메인으로 이동
change_logout();
function login(){
    var link="../main.html";
    location.href=link;
}

// 로그인을 로그아웃으로 변경
// 로그인 -> 로그인페이지로 이동 -> 로그인 성공 -> 로그아웃으로 모든 페이지 변경
// 로그아웃 -> 메인으로 이동 -> 로그인으로 변경 -> jwt 삭제
function change_logout(){
    if(access_token != null){  // 자체 로그인 된 상태
        $(".link_login").css("display","none");
        $(".link_logout").css("display","block");
        $(".link_logout").css("color","#9CC2FF");
    }else{ // 로그아웃 상태
        $(".link_login").css("display","block");
        $(".link_logout").css("display","none");
    }
}

function logout(){
    localStorage.removeItem('x-access-token');
    localStorage.removeItem('x-refresh-token');
/*
    if (!Kakao.Auth.getAccessToken()) {
        alert("Not logged in.");
        return;
    }
    
    Kakao.Auth.logout(function () {
        localStorage.removeItem('my_access');
        localStorage.removeItem('my_refreshToken');
        alert("logout ok\naccess token -> " + Kakao.Auth.getAccessToken());
    });
    */
}

// 마이페이지 로그인 제한
function login_alert1() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="myPage.html";
        location.href=link;
    }
}
// 새 설문 만들기 로그인 제한
function login_alert2() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="makeForm.html";
        location.href=link;
    }
}

// 내가 만든 설문 로그인 제한
function login_alert3() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="mySurvey.html";
        location.href=link;
    }
}

// 관심있는 설문조사 로그인 제한
function login_alert4() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="interestedSurvey.html";
        location.href=link;
    }
}

// 참여한 설문조사 로그인 제한
function login_alert5() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="participatedSurvey.html";
        location.href=link;
    }
}

// 포인트 내역 로그인 제한
function login_alert6() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="point.html";
        location.href=link;
    }
}


// 회원가입 로그인 제한
function login_alert7() {
    if(access_token != undefined || refresh_token == undefined){
        Swal.fire({
            title: `회원가입은 \n 로그아웃한 상태에서 가능합니다.`,
            text: "로그아웃하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                change_logout();
                logout();
                var link="signUp.html";
                location.href=link;
            }
        });
    }else{
        var link="signUp.html";
        location.href=link;
    }
}

// 마이페이지 로그인 제한
function login_alert8() {
    if(access_token == undefined || refresh_token == undefined){
        Swal.fire({
            title: '회원 전용입니다.',
            text: "로그인하시겠습니까?",
            // icon: 'warning',
            customClass: 'swal-wide',
            showCancelButton: true,
            confirmButtonColor: '#4E7FF2',
            cancelButtonColor: '#DBDBDB',
            confirmButtonText: '예',
            cancelButtonText: '아니요'
        }).then((result) => {
            if (result.isConfirmed) {
                var link="login.html";
                location.href=link;
            }
        })
    }else{
        var link="myPage.html";
        location.href=link;
    }
}

// 소셜 로그인
function social_login(){
    fetch(`http://seolmunzip.shop:9000/auth/kakaoLogin?accessToken=${access_token}`, {
        method: "POST",
        headers: {'Content-Type': 'application/json',
            'accessToken' : access_token,
                'refreshToken': refresh_token}
    })
    .then((response) => {
        console.log("api 성공");
        console.log(response);
    })
    .catch((error) => console.log("error", error))
}

