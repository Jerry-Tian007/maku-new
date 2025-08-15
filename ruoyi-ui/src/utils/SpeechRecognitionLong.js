import * as sdk from "microsoft-cognitiveservices-speech-sdk";

// This example requires environment variables named "SPEECH_KEY" and "SPEECH_REGION"
const speechConfig = sdk.SpeechConfig.fromSubscription(process.env.VUE_APP_GPT_SPEECH_KEY, process.env.VUE_APP_GPT_SPEECH_REGION);
speechConfig.speechRecognitionLanguage = "zh-CN";

export function fromArray(fileOfBlob) {
return new Promise((resolve, reject) => {
  let key = sdk.KeywordRecognitionModel.fromFile('');

  let pushStream = sdk.AudioInputStream.createPushStream();

  let audioConfig = sdk.AudioConfig.fromStreamInput(pushStream);
  let speechRecognizer = new sdk.SpeechRecognizer(speechConfig, audioConfig);
  speechRecognizer.recognizeOnceAsync(result => {
    console.log(`RECOGNIZED: Text=${result.text}`);
    speechRecognizer.close();
  });
  });
}

export function sttFromMic() {
  const audioConfig = sdk.AudioConfig.fromDefaultMicrophoneInput();
  const recognizer = new sdk.SpeechRecognizer(speechConfig, audioConfig);

  return recognizer;
}
export function sttFromMicSingle() {
  const audioConfig = sdk.AudioConfig.fromDefaultMicrophoneInput();
  const speechRecognizer = new sdk.SpeechRecognizer(speechConfig, audioConfig);
  return speechRecognizer;
}

