import 'package:shared_preferences/shared_preferences.dart';

/// Persistência mínima do roteiro e preferências de velocidade.
/// O overlay e o app principal leem daqui.
class ScriptStore {
  static const _textKey = 'overlay_script_text';
  static const _speedKey = 'overlay_script_speed';
  static const _fontKey = 'overlay_script_font';
  static const _stayBackKey = 'stay_in_background';

  static Future<String> loadText() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_textKey) ?? '';
  }

  static Future<void> saveText(String text) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_textKey, text);
  }

  static Future<double> loadSpeed() async {
    final prefs = await SharedPreferences.getInstance();
    final raw = prefs.getDouble(_speedKey) ?? 18;
    // Velocidades antigas absurdas (ex. 72+) voltam pro padrão.
    if (raw > 48) return 18;
    return raw.clamp(6, 48);
  }

  static Future<void> saveSpeed(double speed) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setDouble(_speedKey, speed);
  }

  static Future<double> loadFontSize() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getDouble(_fontKey) ?? 22;
  }

  static Future<void> saveFontSize(double size) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setDouble(_fontKey, size);
  }

  /// Enquanto true, a MainActivity se empurra pra trás se ganhar foco
  /// (evita tapar a câmera durante a gravação). Abra o app pelo ícone
  /// do launcher pra editar o roteiro de novo — isso limpa o flag.
  static Future<void> setStayInBackground(bool value) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setBool(_stayBackKey, value);
  }
}
