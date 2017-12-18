$ ->
  $.get "/fullPublications", (fullPublications) ->
    $.each fullPublications, (index, fullPublication) ->
      $("#fullPublications").append $("<li>").text fullPublication.title