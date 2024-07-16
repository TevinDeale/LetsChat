<script setup>
  import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
  import { faUser, faLock } from '@fortawesome/free-solid-svg-icons';
  import { faGithub, faSquareXTwitter } from '@fortawesome/free-brands-svg-icons';
  import { ref } from 'vue';

  const username = ref('');
  const password = ref('');



</script>

<template>
  <main>
    <h1 id="title">#LetsChat</h1>

    <form class="login-form">
      <section class="username-password-input">
        <label for="username"><FontAwesomeIcon :icon="faUser" /></label>
        <input type="text" minlength="5" maxlength="10" required placeholder="Username" name="username" v-model="username">  
      </section>

      <section class="username-password-input">
        <label for="password"><FontAwesomeIcon :icon="faLock" /></label>
        <input type="password" minlength="12" maxlength="20" required placeholder="Password" name="password" v-model="password">
      </section>

      <button id="login-button" type="submit">Login</button>

      <hr />

      <section class="social-login">
        <button class="social-button"><FontAwesomeIcon :icon="faGithub" /></button>
        <button class="social-button"><FontAwesomeIcon :icon="faSquareXTwitter" /></button>
      </section>

    </form>

  </main>
  
</template>

<style lang="scss" scoped>
  main {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    #title {
      font-weight: 800;
    }
  }

  .login-form {
    width: fit-content;
    padding: .5rem;
    .username-password-input {
      border-bottom: 1px solid var(--color-text);
      margin-bottom: 1rem;
      font-size: 1.5rem;

      label {
        padding-right: .3rem;
      }
    }
    input {
      border: none;
      background: transparent;
      color: var(--color-text);
      font-size: 1.5rem;
    }

    #login-button {
      border: none;
      width: 100%;
      padding: .5rem;
      font-size: 1.5rem;

      &:hover {
        cursor: pointer;
        border: 1px solid black;
      }
    }

    hr {
      margin-top: 2rem;
    }
  }

  .social-login {
    display: flex;
    column-gap: 1rem;
    justify-content: center;
    .social-button {
      background: none;
      margin-top: 1rem;
      font-size: 2.5rem;
      border: none;

      &:hover {
        cursor: pointer;
      }
    }
  }


</style>