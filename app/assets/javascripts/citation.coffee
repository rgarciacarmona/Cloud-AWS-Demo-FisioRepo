$ ->
  $.get "/citations", (citations) ->
    $.each citations, (index, citation) ->
      $("#citations").append $("<li>").text citations.name