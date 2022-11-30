function changeLocale(language) {
    let url = window.location.href;
    if (url.includes('?') && !url.includes('?locale')) {
        if (url.includes('&locale')) {
            url = url.slice(0, -2) + language;
            return url;
        } else {
            url = window.location.href + '&locale=' + language;
            return url;
        }
    }


    if (url.includes('?locale')) {
        console.log('fuck you')
        url = url.slice(0, -2) + language;
        return url;
    } else {
        url = window.location.href + '?locale=' + language;
        return url;
    }
}