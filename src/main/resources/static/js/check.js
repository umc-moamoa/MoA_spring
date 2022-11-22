// jwt 관리하는 파일


// 변수 선언
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');
const link_login = document.querySelector(".link_login");
const link_logout = document.querySelector(".link_logout");

// 로그인 후 메인으로 이동
change_logout();
function login(){
    var link="../templates/main.html";
    location.href=link;
}

// 로그인을 로그아웃으로 변경
// 로그인 -> 로그인페이지로 이동 -> 로그인 성공 -> 로그아웃으로 모든 페이지 변경
// 로그아웃 -> 메인으로 이동 -> 로그인으로 변경 -> jwt 삭제
function change_logout(){
    if(my_jwt != null){  // 로그인 된 상태
        $(".link_login").css("display","none");
        $(".link_logout").css("display","block");
    }else{ // 로그아웃 상태
        $(".link_login").css("display","block");
        $(".link_logout").css("display","none");
    }
}
function logout(){
    localStorage.removeItem('x-access-token');
    localStorage.removeItem('x-refresh-token');
}

// 마이페이지 로그인 제한
function login_alert1() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/myPage.html";
        location.href=link;
    }
}
// 새 설문 만들기 로그인 제한
function login_alert2() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/makeForm.html";
        location.href=link;
    }
}

// 내가 만든 설문 로그인 제한
function login_alert3() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/mySurvey.html";
        location.href=link;
    }
}

// 관심있는 설문조사 로그인 제한
function login_alert4() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/interestedSurvey.html";
        location.href=link;
    }
}

// 참여한 설문조사 로그인 제한
function login_alert5() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/participatedSurvey.html";
        location.href=link;
    }
}

// 포인트 내역 로그인 제한
function login_alert6() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/point.html";
        location.href=link;
    }
}


// 회원가입 로그인 제한
// function login_alert7() {
//     if(my_jwt == null){
//         var link="../html/signUp.html";
//         location.href=link;
//     }else{
//         var link="../html/main.html";
//         location.href=link;
//         alert("회원가입은 로그아웃한 상태에서 가능합니다.");
//     }
// }

function login_alert7() {
    if(my_jwt != null){
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
                var link="../templates/signUp.html";
                location.href=link;
            }
        });
    }else{
        var link="../templates/signUp.html";
        location.href=link;
    }
}

// 마이페이지 로그인 제한
function login_alert8() {
    if(my_jwt == null){
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
                var link="../templates/login.html";
                location.href=link;
            }
        })
    }else{
        var link="../templates/myPage.html";
        location.href=link;
    }
}
