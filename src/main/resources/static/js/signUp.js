// 유효성 체크
function join_check(){
    var email = document.getElementById("email");
    var pwd1 = document.getElementById("pswd1");
    var pwd2 = document.getElementById("pswd2");
    var nickName = document.getElementById("nickName");

    // 해당 입력값이 없을 경우
    var idCheck = /^(?=.*[a-zA-Z]).{7,15}$/;
    var id = $("#email").val();
    if(id.value == ""){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("아이디를 입력하세요.");
        email.focus(); 
        return false; 
    }else if(false === idCheck.test(id)){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이메일 인증해주세요.");
        return false;
    }else{
        $(".validId1").css("display","none");
    };
    
    var nickCheck = /^(?=.*[a-zA-Z]).{7,15}$/;
    var nick = $("#nickName").val();
    if (nickName.value == "") {
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("닉네임을 입력하세요.");
        nickName.focus();
        return false;
    }else if(false === nickCheck.test(nick)){
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("닉네임은 영어대소문자 7~15자리로 사용해야합니다.");
        return false;
    }else{
        $(".validId2").css("display","none");
    };

    var pwdCheck = /^(?=.*[a-zA-Z])(?=.*[$@$!%*#?&])(?=.*[0-9]).{7,15}$/;
    var pw = $("#pswd1").val();
    if(pwd1.value == ""){
        $(".validId3").css("display","block");
        $(".validId3").css("color","#FC4B3D");
        $(".validId3").text("비밀번호를 입력하세요.");
        pwd1.focus();
        return false;
    }else if(false === pwdCheck.test(pw)){
        $(".validId3").css("display","block");
        $(".validId3").css("color","#FC4B3D");
        $(".validId3").text("비밀번호는 영어대소문자+숫자+특수문자 조합으로 7~15자리 사용해야 합니다.");
        pwd1.focus();
        return false;
    }
    else{
        $(".validId3").css("display","none");
    };

    if (pwd1.value !== pwd2.value) {
        $(".validId4").css("display","block");
        $(".validId4").css("color","#FC4B3D");
        $(".validId4").text("비밀번호가 일치하지 않습니다.");
        pwd2.focus();
        return false;
    }else{
        $(".validId4").css("display","none");
    };

    if(check_id == 0){
        alert("아이디 중복체크해주세요.");
    }else if(check_name == 0){
        alert("닉네임 중복체크해주세요.");
    }else{
        save(); 
    };

    
}
var check_id = 0;
//아이디 중복체크
/*
function id_check() {
    const data = {
        id: email.value
    }
    fetch(`http://seolmunzip.shop:9000/users/id/${data.id}`, {
        method: "GET",
        headers: {'Content-Type': 'application/json'}
    })

    .then((response) => response.json())
    .then((webResult) => {
        id_check_result(webResult.code);
        windowOpen(); 
        check_email();
    })
    .catch((error) => console.log("error", error))
}
*/
// 이메일 인증 팝업창
function windowOpen(){
    window.open('emailWindow.html', '이메일 인증', 'top=200, left=600, width=450, height=316, status=no, menubar=no, toolbar=no, resizable=no');
}

function check_email(){
    const { OK_id, OK_Stype } = JSON.parse(localStorage.getItem("user-info"));
    localStorage.remove("user-info");
    $(".email").value(OK_id);
    $(".Stype").selected(OK_Stype);
    console.log(OK_id, OK_Stype);
}

function id_check_result(data) {
    var idCheck = /^(?=.*[a-zA-Z]).{7,15}$/;
    var id = $("#email").val();
    if(id.value == ""){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("아이디를 입력하세요.");
        id.focus();
    }else if(false === idCheck.test(id)){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이메일 인증해주세요.");
        check_id = 0;
    }else if(data == "1000"){ 
        $(".validId1").css("display","block");
        $(".validId1").css("color","#4383FF");
        $(".validId1").text("사용 가능한 아이디입니다.");
        check_id = 1;
        
    }else if(data == "2061"){ 
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이미 사용 중인 아이디입니다.");
        check_id = 0;
    }else{
        $(".validId1").css("display","none");
    }
}

var check_name = 0;
//닉네임 중복체크
function name_check() {
    const data = {
        nick : nickName.value
    }
    fetch(`http://seolmunzip.shop:9000/users/nick/${data.nick}`, {
        method: "GET",
        headers: {'Content-Type': 'application/json'}
    })

    .then((response) => response.json())
    .then((webResult) => nick_check_result(webResult.code))
    .catch((error) => console.log("error", error))
}

function nick_check_result(data){
    var nickCheck = /^(?=.*[a-zA-Z]).{7,15}$/;
    var nick = $("#nickName").val();
    if(id.value == ""){
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("닉네임을 입력하세요.");
        id.focus();
    }else if(false === nickCheck.test(nick)){
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("닉네임은 영어대소문자 7~15자리로 사용해야합니다.");
        check_name = 0;
    }else if(data == "1000"){ 
        $(".validId2").css("display","block");
        $(".validId2").css("color","#4383FF");
        $(".validId2").text("사용 가능한 닉네임입니다.");
        check_name = 1;
        
    }else if(data == "2062"){ 
        $(".validId2").css("display","block");
        $(".validId2").css("color","#FC4B3D");
        $(".validId2").text("이미 사용 중인 닉네임입니다.");
        check_name = 0;
    }else{
        $(".validId2").css("display","none");
    };
}

function save(){
    const data = {
        id: document.getElementById("email").value,
        pwd: document.getElementById("pswd1").value,
        nick: document.getElementById("nickName").value
    }
    fetch(`http://seolmunzip.shop:9000/users`, {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })

    .then((response) => response.json())
    .then((response2) => {
        console.log(response2)
        // moveToLogin();
    
    })
    .catch((error) => console.log("error", error))
}

// 회원가입 후 메인으로 이동
function moveToLogin(){
    //alert("회원가입이 완료되었습니다.");

    Swal.fire({
        title: '회원가입이 완료되었습니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    }).then((result) => {
        if (result.isConfirmed) {
            var link="../templates/login.html";
            location.href=link;
        }

    })
}

