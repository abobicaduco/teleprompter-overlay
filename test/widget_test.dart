import 'package:flutter_test/flutter_test.dart';

import 'package:teleprompter_overlay/main.dart';

void main() {
  testWidgets('home renders', (WidgetTester tester) async {
    await tester.pumpWidget(const PromptCueApp());
    expect(find.text('PromptCue'), findsOneWidget);
  });
}
