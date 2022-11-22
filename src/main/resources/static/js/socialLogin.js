var my_jwt = localStorage.getItem('x-access-token');
var my_refreshToken = localStorage.getItem('refreshToken');

// 카카오 소셜로그인
function kakao_login(){
    const CLIENT_ID = "33e6522327646afb407301014936ebe9";
    const REDIRECT_URI =  "http://localhost:9000/auth/kakaoLogin";

    const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URI}&response_type=code`;

    var link=KAKAO_AUTH_URL;
    location.href=link;

    let code = new URL(window.location.href).searchParams.get('code');
    //console.log(code);
}


function social_login(){
    fetch(`http://seolmunzip.shop:9000//users/kakao/${accessToken}`, {
        method: "POST",
        headers: {'x-access-token' : my_jwt,
                'refreshToken': my_refreshToken}
    })
    .then((response) => {
        response.json();
    })
    .catch((error) => console.log("error", error))
}
