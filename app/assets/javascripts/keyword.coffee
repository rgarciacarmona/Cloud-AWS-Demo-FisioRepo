$ ->
  $.get "/keywords", (keywords) ->
    $.each keywords, (index, keyword) ->
      $("#keywords").append $("<li>").text keywords.name