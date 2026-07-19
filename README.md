# PromptCue

Teleprompter em overlay para gravação de vídeos no Android. Fica flutuando por cima da câmera nativa (ou de qualquer outro app) enquanto você grava, com o texto rolando de baixo pra cima numa faixa fixa e discreta no centro da tela, sem precisar desviar o olho pra ler.

## Download

Baixe o APK pronto na [página de releases](https://github.com/caducosilva/teleprompter-overlay/releases/latest) e instale direto no Android (é preciso permitir instalação de fontes desconhecidas).

## Como funciona

1. Abra o PromptCue, cole o roteiro e toque em **Abrir teleprompter**.
2. O app fecha sozinho e deixa só a faixa do teleprompter flutuando na tela.
3. Abra a câmera (ou qualquer app de gravação) e dê play na faixa — o texto rola sozinho.
4. Pra editar o roteiro de novo, toque no ícone do PromptCue no launcher.

## Recursos

- Faixa fixa, não arrastável, posicionada um pouco acima do centro, perto de onde fica a lente frontal, pra parecer que você está olhando pra quem assiste.
- Play/pausa com retomada de onde parou; ao terminar o roteiro, o play seguinte reinicia do começo.
- Scroll manual com o dedo quando pausado.
- Controle de velocidade e tamanho da fonte, salvos entre sessões.
- A tela do app se esconde sozinha enquanto o overlay está ativo, pra nunca tampar a câmera durante a gravação.

## Stack

Flutter + [`flutter_overlay_window`](https://pub.dev/packages/flutter_overlay_window) para o overlay do sistema, `shared_preferences` para persistir roteiro/velocidade/fonte, e `wakelock_plus` pra manter a tela acesa durante a gravação.

## Rodando localmente

```bash
flutter pub get
flutter run
```

Requer permissão de "Aparecer sobre outros apps" no Android, concedida na primeira vez que o overlay é aberto.

## Licença

MIT — veja [LICENSE](LICENSE).

## Autor

**CADUCOSILVA** — [Carlos Eduardo (@caducosilva)](https://github.com/caducosilva)  
Contato: abobicarlo@gmail.com

Doações via PIX (chave aleatória): `f74458dc-2a36-49bd-9250-1cef4365ebb8`
