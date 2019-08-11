var localStorage = window.localStorage;

document.addEventListener('DOMContentLoaded', function() {
   startupMethod();
}, false);

function startupMethod() {
  checkLogin();
}

function checkLogin() {
  if (!isUserLoggined()) {
    showLoginForm();
  } else {
    loadUserTasks();
  }
}

function handleRegister() {
  processRegisteration();
}

function handleLogin() {
  processLogin();
}

function handleLogout() {
  logout();
}

function logout() {
  localStorage.removeItem('loggedIn');
  localStorage.removeItem('userId');
  document.location.reload(true);
}

function processRegisteration() {
  var loginData = getLoginFormData();
  if (!(isEmpty(loginData.username) || isEmpty(loginData.password))) {
    post(BASE_URL+'/auth/registration', loginData)
      .then(obj => {
        get(BASE_URL+'/auth/me', {'Authorization': 'Bearer '+obj.authToken})
          .then(user => {
            login(user.id, user.username, obj.authToken);
          });
      });
  }
}

function processLogin() {
  var loginData = getLoginFormData();
  if (!(isEmpty(loginData.username) || isEmpty(loginData.password))) {
    post(BASE_URL+'/auth/login', loginData)
      .then(obj => {
        get(BASE_URL+'/auth/me', {'Authorization': 'Bearer '+obj.authToken})
          .then(user => {
            login(user.id, user.username, obj.authToken);
          });
      });
  }
}

function login(userId, username, authToken) {
  localStorage.setItem('loggedIn', true);
  localStorage.setItem('userId', userId);
  localStorage.setItem('authToken', authToken);
  loadUserTasks();
  closeLoginForm();
}

function showLoginForm() {
  document.getElementById('login-form').style.display='block';
}

function closeLoginForm() {
  document.getElementById('login-form').style.display='none';
}

function getLoginFormData() {
  var username = document.getElementById('username-input').value;
  var password = document.getElementById('password-input').value;
  return { username: username, password: password };
}

function isUserLoggined() {
  var loggedIn = localStorage.getItem('loggedIn');
  var userId = localStorage.getItem('userId');
  return (!isEmpty(userId) || isTrue(loggedIn));
}
