<template>
  <div>
    <div ref="flipbook" id="flipbook"></div>
  </div>
</template>

<script setup>
  import { ref, watch, nextTick, onMounted } from "vue";

  const props = defineProps({
    submissions: {
      type: Array,
      required: true,
    },
  });

  const flipbook = ref(null);

  const buildFlipbookPages = () => {
    const htmlPages = [];

    // Dummy-Seite vorne, damit Turn.js mit einer Doppelseite beginnt
    htmlPages.push(`<div class="page empty-page"></div>`);

    // Eigentliche Bilderseiten
    props.submissions.forEach((sub) => {
      htmlPages.push(`
      <div class="page">
        <div class="page-header">${sub.date}, ${sub.user}</div>
        <div class="photo-wrapper">
          <img src="${sub.photoUrl}" />
          <div class="caption">${sub.caption}</div>
        </div>
      </div>
    `);
    });

    return htmlPages.join("");
  };

  //

  const initFlipbook = async () => {
    if (!flipbook.value || !window.$) return;

    // Vorherige Instanz zerstören
    if (window.$(flipbook.value).data("turn")) {
      window.$(flipbook.value).turn("destroy");
    }

    // DOM befüllen
    flipbook.value.innerHTML = buildFlipbookPages();

    // Warte darauf, dass Vue den DOM aktualisiert
    await nextTick();

    // Jetzt flipbook initialisieren
    window.$(flipbook.value).turn({
      width: 800,
      height: 600,
      autoCenter: true,
      elevation: 50,
      gradients: true,
      display: "double",
      page: 2,
    });
  };

  onMounted(async () => {
    console.log(props.submissions.length);
    if (props.submissions.length) {
      initFlipbook();
    }
  });

  watch(
    () => props.submissions,
    (newSubs) => {
      if (newSubs.length) {
        initFlipbook();
      } else if (flipbook.value) {
        flipbook.value.innerHTML = "";
      }
    }
  );
</script>

<style>
  #flipbook {
    width: 100%;
    max-width: 1000px;
    margin: auto;
    background-image: url("../assets/diary-book-bg.png");
    background-repeat: no-repeat;
    background-position: 47% 53%;
    background-size: 1200px auto;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  /* Flipbook-Seite */
  .page {
    width: 100%;
    height: 100%;
    overflow: hidden;
    background: transparent;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  /* Wrapper um das Bild */
  .photo-wrapper {
    position: relative;
    width: 230px;
    height: 300px;
    background: white;
    border-radius: 3px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.11);
    padding: 30px 10px 70px 10px;
    display: flex;
    margin-bottom: 50px;
    left: -0.75%;
    position: relative; /* für das absolute Overlay */
    overflow: visible;
  }

  /* Bild selbst */
  .photo-wrapper img {
    width: 210px;
    height: auto;
    position: absolute;
  }

  .photo-wrapper .caption {
    position: absolute;
    left: 25%;
    top: 244px;
    margin-left: 20px;
    bottom: -60px;
    transform: translateX(-30%);
    width: 98%;
    text-align: left;
    font-family: "Pacifico", cursive;
    font-size: 1.1em;
    color: #444;
    background: transparent;
    pointer-events: none;
    z-index: 20;
    white-space: pre-line;
  }

  .page-header {
    position: absolute;
    top: 120px;
    left: 60%;
    transform: translateX(-30%);
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: transparent;
    border-radius: 8px;
    padding: 6px 14px;
    font-size: 1em;
    font-family: "Pacifico", cursive; /* oder eine andere Handschrift */
    color: #333;
    z-index: 20;
    pointer-events: none;
  }

  /*.photo-wrapper::before {
    content: "";
    position: absolute;
    top: -110px;
    left: -133px;
    width: 220%;
    height: 220%;
    pointer-events: none; /* Klicks kommen durch */
  /* background: url("/polaroid.png") no-repeat center center;
    background-size: contain;
    z-index: 10;
  }*/

  .empty-page {
    background: transparent !important;
    box-shadow: none !important;
  }
</style>
