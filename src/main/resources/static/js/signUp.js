var emailInput = document.getElementById("email");
var num = document.getElementById("num");
var emailVal;
var certifiedCode = localStorage.getItem('certifiedCode');
var email_check_num;

// 유효성 체크
function join_check(){
    var email = document.getElementById("email");
    var num = document.getElementById("num");
    var pwd1 = document.getElementById("pswd1");
    var pwd2 = document.getElementById("pswd2");
    var nickName = document.getElementById("nickName");

    // 해당 입력값이 없을 경우
    // 아이디(이메일)
    var id = $("#email").val();
    if(id.value == ""){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("아이디를 입력하세요.");
        email.focus(); 
        return false; 
    }else{
        $(".validId1").css("display","none");
    };
    // 인증번호
    var num = $("#num").val();
    if(num.value == ""){
        $(".validId5").css("display","block");
        $(".validId5").css("color","#FC4B3D");
        $(".validId5").text("인증번호를 입력하세요.");
        num.focus(); 
        return false; 
    }else{
        $(".validId5").css("display","none");
    };
    
    // 닉네임
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

    // 비밀번호
    var pwdCheck = /^(?=.*[a-zA-Z])(?=.*[$@$!%*#?&^.])(?=.*[0-9]).{7,15}$/;
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

    // 비밀번호 확인
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
        alert("이메일 인증해주세요.");
    }else if(check_name == 0){
        alert("닉네임 중복체크해주세요.");
    }
    else{
        save(); 
    };

    
}
var check_id = 0;

// 이메일 인증(이메일 보내기)
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

function check_send_email(data){
    if(email.value == ""){
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이메일을 입력하세요.");
        email.focus();
    }else{
        $(".validId1").css("display","none");
    };
}


// 이메일 인증(인증번호 확인)
function email_num(){
    inputCertifiedCode = num.value;
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
        $(".validId5").css("display","block");
        $(".validId5").css("color","#FC4B3D");
        $(".validId5").text("인증번호를 입력하세요.");
        num.focus();
    }else if(data == "1000"){ 
        $(".validId5").css("display","block");
        $(".validId5").css("color","#4383FF");
        $(".validId5").text("인증되었습니다.");
        email_check_num = 1;
    }
    else if(data == "2063"){
        $(".validId5").css("display","block");
        $(".validId5").css("color","#FC4B3D");
        $(".validId5").text("인증번호를 확인해주세요.");
        num.focus();
    }
    else{
        $(".validId5").css("display","none");
    };
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
        $(".validId1").text("사용 가능한 이메일입니다.");
        check_id = 1;
    }else if(data == "2061"){ 
        $(".validId1").css("display","block");
        $(".validId1").css("color","#FC4B3D");
        $(".validId1").text("이미 사용 중인 이메일입니다.");
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
    if(nick.value == ""){
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
        email: document.getElementById("email").value,
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
        
        if(response2.code == 2006){
            $(".validId1").css("display","block");
            $(".validId1").css("color","#FC4B3D");
            $(".validId1").text("이메일 글자수를 확인해주세요.");
            console.log(response2.isSuccess);
        } else if(response2.code == 2007) {
            $(".validId2").css("display","block");
            $(".validId2").css("color","#FC4B3D");
            $(".validId2").text("닉네임은 영어대소문자 7~15자리로 사용해야합니다.");
        }else if(response2.code == 2008) {
            $(".validId3").css("display","block");
            $(".validId3").css("color","#FC4B3D");
            $(".validId3").text("비밀번호는 영어대소문자+숫자+특수문자 조합으로 7~15자리 사용해야 합니다.");
        }else if(response2.code == 2061) {
            $(".validId1").css("display","block");
            $(".validId1").css("color","#FC4B3D");
            $(".validId1").text("중복된 이메일입니다.");
        }else if(response2.code == 2062) {
            $(".validId2").css("display","block");
            $(".validId2").css("color","#FC4B3D");
            $(".validId2").text("중복된 닉네임입니다.");
        }
        else{
            moveToLogin();
        }
    
    })
    .catch((error) => console.log("error", error))
}

// 회원가입 후 메인으로 이동
function moveToLogin(){
    Swal.fire({
        title: '회원가입이 완료되었습니다.',
        customClass: 'swal-wide',
        confirmButtonColor: '#4E7FF2',
        cancelButtonColor: '#DBDBDB',
        confirmButtonText: '예'
    }).then((result) => {
        if (result.isConfirmed) {
            var link="../login.html";
            location.href=link;
        }

    })
}

