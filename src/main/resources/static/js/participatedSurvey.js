const $SurveyList = document.querySelector("#SurveyList");
var my_jwt = localStorage.getItem('x-access-token');
var my_refresh = localStorage.getItem('x-refresh-token');

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

const fetchParticipate = () => {
    var requestOptions = {
        method: "Get",
        headers: {'x-access-token' : my_jwt,  'REFRESH-TOKEN' : my_refresh,}
    };

    fetch(
        "http://seolmunzip.shop:9000/users/partPost",
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => {
            console.log(webResult.code);
            if(webResult.code == 1000) {
                webResult.result.map(item => ParticipateListTemplate(item));
                if(webResult.result.length != 0){
                    $(".length_zero_txt").css("display","none");
                }
                slick();
            }
            if(webResult.code == 2002) {
                fetchTokenCheck();
                var requestOptions = {
                    method: "Get",
                    headers: {'x-access-token' : my_jwt,  'REFRESH-TOKEN' : my_refresh,}
                };
            
                fetch(
                    "http://seolmunzip.shop:9000/users/partPost",
                    requestOptions
                )
                    .then((response) => response.json())
                    .then((webResult) => {
                        console.log(webResult.code);
                        if(webResult.code == 1000) {
                            webResult.result.map(item => ParticipateListTemplate(item));
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

fetchParticipate();

function ParticipateListTemplate(data) {
    const sendPostId = data.postId;
    const ParticipatedItem1 = `
        <div id="main1">
            <div class="one-container1">
                <a id="title1" href="../templates/detailPage.html?${sendPostId}">  ${data.title}  </a>
            </div>
            <div class="two-container1">
                <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
                <span id="type1">ㅣ&nbsp;&nbsp;D-${data.dday}</span>
            </div>
            <div class="three-container1">
                <span id="point1">  ${data.point}P  </span>
            </div>
        </div>
    `;
    const ParticipatedItem2 = `
        <div id="main1">
            <div class="one-container1">
                <a id="title1" href="../templates/detailPage.html?${sendPostId}">  ${data.title}  </a>
            </div>
            <div class="two-container1">
                <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
                <span id="type1">ㅣ&nbsp;&nbsp; CLOSED</span>
            </div>
            <div class="three-container1">
                <span id="point1">  ${data.point}P  </span>
            </div>
        </div>
    `;
    const ParticipatedItem3 = `
        <div id="main1">
            <div class="one-container1">
                <a id="title1" href="../templates/detailPage.html?${sendPostId}">  ${data.title}  </a>
            </div>
            <div class="two-container1">
                <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
                <span id="type1">ㅣ&nbsp;&nbsp; D-DAY</span>
            </div>
            <div class="three-container1">
                <span id="point1">  ${data.point}P  </span>
            </div>
        </div>
    `;
    if(data.status =='CLOSED'){
        $SurveyList.insertAdjacentHTML('beforeend', ParticipatedItem2);
    }else if(data.dday == 0){
        $SurveyList.insertAdjacentHTML('beforeend', ParticipatedItem3);
    }else{
        $SurveyList.insertAdjacentHTML('beforeend', ParticipatedItem1);
    }

}

function slick(){
    $(document).ready(function(){
        $('.SurveyList').slick({
            slidesToShow: 2,
            slidesToScroll: 1,
            rows:3,
            infinite:false,
            prevArrow : $('.prev'), 
            nextArrow : $('.next'), 
        });
    });
}