import { ref, computed, reactive } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router';

export const useChatStore = defineStore('chat', () => {
  const testRef = ref('test')
  const file = ref()
  const host = 'http://localhost:9000/'

  console.log("store loaded");
  const loggedIn = reactive({
    userId: '',
    username: 'Guest',
  })
  const credentialsForm = reactive({
    username: '',
    password: '',
  })
  const messageJson = reactive({
    userId: '',
    username: '',
    message: '',
    photoType: '',
    photoBytes: '',
  })

  const defaultLoggedIn = () => {
    loggedIn.userId = ''
    loggedIn.username = 'Guest'
  }

  const clearCredentialsForm = () => {
    credentialsForm.username = ''
    credentialsForm.password = ''
  }
  const clearMessageJson = () => {
    messageJson.userId = ''
    messageJson.username = ''
    messageJson.message = ''
    messageJson.photoType = ''
    messageJson.photoBytes = ''
  }

  const register = () => {
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentialsForm)
    };
    fetch(`${host}register`, requestOptions)
      .then(response => {
        if (!response.ok) {
          alert("Registration failed.");
          clearCredentialsForm()
        }
        alert("Registration Successful.");
        clearCredentialsForm()
        router.push('/login')
      })
      .catch(error => console.error('Error registering: ', error));
  }
  const login = () => {
    console.log(`login started. username: ${credentialsForm.username} password: ${credentialsForm.password}`)
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username: credentialsForm.username, password: credentialsForm.password }),
      credentials: 'include'
    };
    fetch(`${host}login`, requestOptions)
      .then(response => {
        if (!response.ok) {
          alert("Login failed.");
        } else {
          return response.json()
        }
      })
      .then(data => {
        loggedIn.userId = data.userId
        loggedIn.username = data.username
        clearCredentialsForm()
        console.log(`login succeeded. userId: ${loggedIn.userId} username: ${loggedIn.username}`)
        router.push('/dashboard')
      })
      .catch(error => console.error('Error logging in: ', error));
  }
  const logout = () => {
    fetch(`${host}logout`, {method: "POST", credentials:'include'})
      .then(response => {
        if (!response.ok) {
          alert("Not logged in.");
        } else {
          alert("You have been logged out.");
          defaultLoggedIn()
          router.push('/')
        }
      })
      .catch(error => console.error('Error logging out: ', error));
  }

  return {
    testRef,
    loggedIn,
    credentialsForm,
    messageJson,
    register,
    login,
    logout
  }

})
