var emailInput = document.getElementById("email");
var emailVal;
var certifiedCode = localStorage.getItem('certifiedCode');
var email_check_num;
var OK_id;

// 이메일 인증(이메일 전송)
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
        check_send_email(webResult.code);
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
    fetch(`http://seolmunzip.shop:9000/email/auth?certifiedCode=${certifiedCode}`, {
        method: "POST",
        headers: {'Content-Type': 'application/json', 'certifiedCode': certifiedCode}
    })
    .then((response) => response.json())
    .then((response2) => {
        //console.log(response2);
        //check_email_num(response2.code);
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


// 이메일 인증 완료
function email_OK(){
    if(email_check_num == 1){
        // 인증 완료하면 창 닫고, 인증된 이메일 회원가입 창에 올리기
        //window.close();
        
        localStorage.removeItem('OK_id');
        localStorage.setItem('OK_id',email.value);
        certifiedCode = localStorage.getItem('OK_id');
        
    }else{
        alert("이메일 인증을 완료해주세요.");
    };
}