// Define our button click listener
$(window).on("load", function () {
  // Loading Buttons from menu
  valueIndex(1);
  // Loading Button from profile
  $("#btnMoreFoundGames").on("click", () =>
    functionMoreFoundGames(
      "#moreFoundGames",
      "#loaderFoundGame",
      "#btnMoreFoundGames"
    )
  );
  $("#btnGamesReview").on("click", () =>
    functionMoreFoundReviews(
      "#moreReviewsGame",
      "#loaderGameReview",
      "#btnGamesReview"
    )
  );
  $("#btnMoreIndexGames").on("click", () =>
    functionMoreIndexGames(
      "#moreIndexGames",
      "#loaderIndexGame",
      "#btnMoreIndexGames"
    )
  );
  $("#btnMoreCartGames").on("click", () =>
    functionMoreCartGames(
      "#moreCartGames",
      "#loaderCartGame",
      "#btnMoreCartGames"
    )
  );
  $("#btnMoreCheckGames").on("click", () =>
    functionMoreCheckGames(
      "#moreCheckGames",
      "#loaderCheckGame",
      "#btnMoreCheckGames"
    )
  );
  $("#btnMoreControlGames").on("click", () =>
    functionMoreControlGames(
      "#moreControlGames",
      "#loaderControlGame",
      "#btnMoreControlGames"
    )
  );
});

var indexFoundGames;
var indexGameReviews;
var indexIndexGames;
var indexCartGames;
var indexCheckGames;
var indexControlGames;

function ajaxCall(url, spinner, where, button) {
  $.ajax({
    type: "GET",
    contenType: "aplication/json",
    url: url,
    beforeSend: function () {
      $(spinner).removeClass("hidden");
    },
    success: function (result) {
      $(where).append(result);
    },
    error: function (e) {
      $(button).addClass("hidden");
    },
    complete: function () {
      $(spinner).addClass("hidden");
    },
  });
}

function functionMoreIndexGames(where, spinner, button) {
  value = indexIndexGames;
  this.indexIndexGames += 1;
  url = "/moreIndexGames/" + value;
  ajaxCall(url, spinner, where, button);
}

function functionMoreControlGames(where, spinner, button) {
  value = indexControlGames;
  this.indexControlGames += 1;
  url = "/moreControlGames/" + value;
  ajaxCall(url, spinner, where, button);
}

function functionMoreCartGames(where, spinner, button) {
  value = indexCartGames;
  this.indexCartGames += 1;

  // Search parameter in url
  const arrayPath = window.location.pathname.split("/");
  const id = arrayPath[2];
  url = "/moreCartGames/" + id + "/" + value;
  ajaxCall(url, spinner, where, button);
}

function functionMoreCheckGames(where, spinner, button) {
  value = indexCheckGames;
  this.indexCheckGames += 1;

  // Search parameter in url
  const arrayPath = window.location.pathname.split("/");
  const id = arrayPath[2];
  url = "/moreCheckGames/" + id + "/" + value;
  ajaxCall(url, spinner, where, button);
}

function functionMoreFoundReviews(where, spinner, button) {
  value = indexGameReviews;
  this.indexGameReviews += 1;

  // Search parameter in url
  const arrayPath = window.location.pathname.split("/");
  const id = arrayPath[2];
  url = "/moreReviews/" + id + "/" + value;
  ajaxCall(url, spinner, where, button);
}

function functionMoreFoundGames(where, spinner, button) {
  const urlParams = new URLSearchParams(window.location.search);
  value = indexFoundGames;
  this.indexFoundGames += 1;

  // Search parameter in url
  var name = urlParams.get("name");
  var category = urlParams.get("category");

  url = "/moreFoundGames/" + value + "?category=" + category + "&name=" + name;
  ajaxCall(url, spinner, where, button);
}

function valueIndex(num) {
  this.indexFoundGames = num;
  this.indexGameReviews = num;
  this.indexIndexGames = num;
  this.indexCartGames = num;
  this.indexCheckGames = num;
  this.indexControlGames = num;
}
