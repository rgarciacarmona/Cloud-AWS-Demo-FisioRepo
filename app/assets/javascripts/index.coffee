$ ->
  $.get "/authors", (authors) ->
    $.each authors, (index, author) ->
      $("#authors").append $("<li>").text author.shortName