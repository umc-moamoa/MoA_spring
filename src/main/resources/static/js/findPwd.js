var emailInput = document.getElementById("email");
var emailVal;
var emailNum = document.getElementById("num");
var email_check_num;

// 존재하는 회원인지 확인


// 이메일 인증
function send_email() {
    emailVal = emailInput.value;

    var requestOptions = {
        method: "GET",
    };

    fetch(`http://seolmunzip.shop:9000/email/send?email=${emailVal}`,
        requestOptions
    )

    .then((response) => response.json())
    .then((webResult) => {
        //console.log(webResult);
        localStorage.removeItem('certifiedCode');
        localStorage.setItem('certifiedCode', webResult.result);
        certifiedCode = localStorage.getItem('certifiedCode');
        console.log(certifiedCode);
        //check_send_email(webResult.code);
        id_check_result(webResult.code);
    })
    .catch((error) => console.log("error", error));
}

function id_check_result(data) {
    var id = $("#email").val();
    if(id.value == ""){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이메일을 입력하세요.");
        id.focus();
    }else if(data == "1000"){ 
        $(".validId1").css("display","block");
        $(".validId1").css("color","#4383FF");
        $(".validId1").text("인증번호가 메일로 전송되었습니다.");
        check_id = 1;
    }else{
        $(".validId1").css("display","none");
    }
}

// 인증번호 확인
function email_num(){
    inputCertifiedCode = emailNum.value;
    fetch(`http://seolmunzip.shop:9000/email/auth?certifiedCode=${inputCertifiedCode}`, {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'certifiedCode': certifiedCode}
    })
    .then((response) => response.json())
    .then((response2) => {
        console.log(response2);
        check_email_num(response2.code);
    })
    .catch((error) => console.log("error", error))
}

function check_email_num(data){
    if(data == ""){
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("인증번호를 입력하세요.");
        num.focus();
    }else if(data == "1000"){ 
        $(".validId2").css("display","block");
        $(".validId2").css("color","#4383FF");
        $(".validId2").text("인증되었습니다.");
        email_check_num = 1;
    }
    else if(data == "2063"){
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("인증번호를 확인해주세요.");
        num.focus();
    }
    else{
        $(".validId2").css("display","none");
    };
}

// 비밀번호 변경
function change_pwd(){
    const data = {
        email: document.getElementById("email").value,
        pwd: document.getElementById("pwd").value
    }
    fetch(`http://seolmunzip.shop:9000/users/pwd`, {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    .then((response) => response.json())
    .then((response2) => {
        console.log(response2);
        if(response2.code == 1000){
            moveToLogin();
        }
        else if(response2.code == 2064) {
            noUser();
        }
        else if(response2.code == 2008) {
            noPwdRule();
        }
        else {
            noPwd();
        }
    })
    .catch((error) => console.log("error", error))
}

// 인증번호 예외처리
function pwdOK() {
    var num = document.getElementById("num");

    if(num.value ==""){
        certifiyAlert();
    }else{
        if(email_check_num ==1){
            change_pwd();
        }else {
            certifiyAlert();
        }
    }
}

// 비밀번호 변경 후 창 닫기
function moveToLogin(){
    Swal.fire({
        title: '비밀번호 변경이 완료되었습니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    }).then((result) => {
        if (result.isConfirmed) {
            window.close();
        }

    })
}

//이메일 인증 alert custom
function certifiyAlert(){
    Swal.fire({
        title: '이메일 인증해주세요.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    })
}

//비밀번호 변경 실패(존재하지 않는 회원일때) alert custom
function noUser() {
    Swal.fire({
        title: '존재하지 않는 회원입니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    })
}

//비밀번호 변경 실패(비밀번호 글자수 조건X) alert custom
function noPwdRule() {
    Swal.fire({
        title: '비밀번호는 영어대소문자+숫자+특수문자 조합으로 7~15자리 사용해야 합니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    })
}

//비밀번호 변경 실패(그밖의 모든 경우) alert custom
function noPwd() {
    Swal.fire({
        title: '비밀번호 변경에 실패했습니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    })
}