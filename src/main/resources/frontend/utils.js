const BASE_URL = 'http://localhost:8080';
var localStorage = window.localStorage;

function post(url, data = {}, headers = {}) {
  return fetch(url, {
        method: 'POST',
        headers: setDefaultHeaders(headers),
        body: JSON.stringify(data)
    })
    .then(checkStatus)
    .then(response => response.json());
}

function remove(url, headers = {}) {
    return fetch(url, {
        method: 'DELETE',
        headers: setDefaultHeaders(headers),
    })
    .then(checkStatus);
}

function get(url, headers = {}) {
  return fetch(url, {
    method: 'GET',
    headers: setDefaultHeaders(headers)
  })
  .then(checkStatus)
  .then(response => response.json());
}

function patch(url, body, headers = {}) {
  return fetch(url, {
    method: 'PATCH',
    headers: setDefaultHeaders(headers)
  })
  .then(checkStatus)
  .then(response => response.json());
}

function setDefaultHeaders(givenHeaders) {
  if (!('Authorization' in givenHeaders))
    if (!isEmpty(localStorage.getItem('authToken')))
      givenHeaders['Authorization'] = 'Bearer '+localStorage.getItem('authToken')

  if (!('Content-Type' in givenHeaders))
    givenHeaders['Content-Type'] = 'application/json'

  return givenHeaders;
}

function checkStatus(res) {
    if (!res.ok) {
        throw new Error('Something went wrong');
    }
    return res;
}

function isEmpty(str) {
    return (!str || 0 === str.length);
}

function isTrue(value) {
    return !!value;
}
