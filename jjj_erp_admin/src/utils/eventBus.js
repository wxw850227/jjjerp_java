import { reactive } from "vue";

const eventBus = reactive({});

eventBus.on = (event, handler) => {
  if (!eventBus[event]) {
    eventBus[event] = [];
  }
  eventBus[event].push(handler);
};

eventBus.off = (event, handler) => {
  if (!eventBus[event]) return;

  const index = eventBus[event].indexOf(handler);
  if (index > -1) {
    eventBus[event].splice(index, 1);
  }
};

eventBus.emit = (event, ...args) => {
  if (!eventBus[event]) return;

  eventBus[event].forEach(handler => handler(...args));
};

export default eventBus;
