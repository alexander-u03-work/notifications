const form = document.querySelector('#demo');
const launch = document.querySelector('#launch');
const status = document.querySelector('#status');
const steps = [...document.querySelectorAll('#flow li')];
const log = document.querySelector('#events');
const receipt = document.querySelector('#receipt');
const messageField = document.querySelector('#message');

const pause = () => new Promise(resolve => setTimeout(resolve, 550));

function showStatus(label, tone) {
  status.textContent = label;
  status.dataset.tone = tone;
}

function record(message) {
  const entry = document.createElement('li');
  const timestamp = document.createElement('time');
  const now = new Date();
  timestamp.dateTime = now.toISOString();
  timestamp.textContent = now.toLocaleTimeString('ru-RU');
  entry.append(timestamp, message);
  log.append(entry);
}

async function runStep(index, message) {
  const step = steps[index];
  step.dataset.state = 'active';
  record(message);
  await pause();
  step.dataset.state = 'done';
}

messageField.addEventListener('input', () => messageField.setCustomValidity(''));

form.addEventListener('submit', async event => {
  event.preventDefault();
  if (launch.disabled) return;

  const message = messageField.value.trim();
  if (!message) {
    messageField.setCustomValidity('Введите текст уведомления.');
    messageField.reportValidity();
    return;
  }

  const scenario = document.querySelector('#scenario').value;
  launch.disabled = true;
  receipt.hidden = true;
  log.replaceChildren();
  steps.forEach(step => delete step.dataset.state);
  showStatus('В обработке', 'active');

  try {
    await runStep(0, 'Тестовый запрос принят.');
    await runStep(1, 'Сообщение помещено в очередь.');
    await runStep(2, 'Обработчик подготовил сообщение.');
    steps[3].dataset.state = 'active';
    record('Выполняется попытка доставки.');
    await pause();

    if (scenario === 'retry') {
      steps[3].dataset.state = 'error';
      record('Временный сбой. Запланирована повторная попытка.');
      await pause();
      await runStep(3, 'Повторная попытка доставки.');
    }

    if (scenario === 'failure') {
      steps[3].dataset.state = 'error';
      record('Канал доставки отклонил сообщение.');
      showStatus('Ошибка доставки', 'bad');
      return;
    }

    steps[3].dataset.state = 'done';
    record('Сообщение доставлено в симуляции.');
    receipt.querySelector('p').textContent = message;
    receipt.hidden = false;
    showStatus('Доставлено', 'good');
  } finally {
    launch.disabled = false;
  }
});
