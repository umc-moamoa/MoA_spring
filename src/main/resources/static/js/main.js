// 인기 있는 설문조사
const $SurveyList1 = document.querySelector("#SurveyList1");

const fetchSurvey1 = () => {
    var requestOptions = {
        method: "GET",
    };

    fetch(
        `http://seolmunzip.shop:9000/posts/desc`,
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => webResult.result.map(item => SurveyListTemplate1(item)))
        .then((webResult) => slick1())
        .catch((error) => console.log("error", error));
}

fetchSurvey1();

function SurveyListTemplate1 (data) {
    const sendPostId = data.postId;

    const SurveyItem1 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;D-${data.dday}</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;
    const SurveyItem2 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;D-DAY</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;
    const SurveyItem3 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;CLOSED</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;

    if ( data.status == 'CLOSED' ) {
        $SurveyList1.insertAdjacentHTML('beforeend', SurveyItem3);
    }else if(data.dday == 0){
        $SurveyList1.insertAdjacentHTML('beforeend', SurveyItem2);
    }else{
        $SurveyList1.insertAdjacentHTML('beforeend', SurveyItem1);
    }

}

// 참여를 기다리고 있는 설문조사
const $SurveyList2 = document.querySelector("#SurveyList2");

const fetchSurvey2 = () => {
    var requestOptions = {
        method: "GET",
    };

    fetch(
        `http://seolmunzip.shop:9000/posts/asc`,
        requestOptions
    )
        .then((response) => response.json())
        .then((webResult) => webResult.result.map(item => SurveyListTemplate2(item)))
        .then((webResult) => slick2())
        .catch((error) => console.log("error", error));
}

fetchSurvey2();

function SurveyListTemplate2 (data) {
    const sendPostId = data.postId;

    const SurveyItem1 = 
    `<div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;D-${data.dday}</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;
    const SurveyItem2 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;D-DAY</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;
    const SurveyItem3 = `
    <div id="main1">
        <div class="one-container1">
            <a id="title1" href="../templates/detailPage.html?${sendPostId}">${data.title}</a>
        </div>
        <div class="two-container1">
            <span id="count1">${data.qcount}개 항목&nbsp;&nbsp;</span>
            <span id="type1">ㅣ&nbsp;&nbsp;CLOSED</span>
        </div>
        <div class="three-container1">
            <span id="point1">${data.point}P</span>
        </div>
    </div> `;

    if ( data.status == 'CLOSED' ) {
        $SurveyList2.insertAdjacentHTML('beforeend', SurveyItem3);
    }else if(data.dday == 0){
        $SurveyList2.insertAdjacentHTML('beforeend', SurveyItem2);
    }else{
        $SurveyList2.insertAdjacentHTML('beforeend', SurveyItem1);
    }
}

function slick1(){
    $(document).ready(function(){
        $('.SurveyList1').slick({
            slidesToShow: 3,
            slidesToScroll: 1,
            rows:1,
            infinite: false,
            prevArrow : $('.prev'), 
            nextArrow : $('.next'), 
        });
    });
}
function slick2(){
    $(document).ready(function(){
        $('.SurveyList2').slick({
            slidesToShow: 3,
            slidesToScroll: 1,
            rows:1,
            infinite: false,
            prevArrow : $('.prev2'), 
            nextArrow : $('.next2'), 
        });
    });
}