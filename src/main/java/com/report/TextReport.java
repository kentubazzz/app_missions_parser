package com.report;

import com.model.*;

import java.text.NumberFormat;
import java.util.Locale;

public class TextReport implements ReportGenerator {

    @Override
    public String generate(Mission mission) {
        StringBuilder sb = new StringBuilder();

        sb.append("МИССИЯ\n");
        sb.append("ID: ").append(mission.getMissionId()).append("\n");
        sb.append("Дата: ").append(mission.getDate()).append("\n");
        sb.append("Место: ").append(mission.getLocation()).append("\n");
        sb.append("Результат: ").append(mission.getOutcome()).append("\n");
        sb.append("Ущерб: ").append(formatCurrency(mission.getDamageCost())).append("\n");
        sb.append("\n");

        if (mission.getCurse() != null) {
            sb.append("ПРОКЛЯТИЕ\n");
            sb.append("Имя: ").append(mission.getCurse().getName()).append("\n");
            sb.append("Уровень: ").append(mission.getCurse().getThreatLevel()).append("\n");
            sb.append("\n");
        }

        if (mission.getSorcerers() != null && !mission.getSorcerers().isEmpty()) {
            sb.append("МАГИ\n");
            for (int i = 0; i < mission.getSorcerers().size(); i++) {
                Sorcerer s = mission.getSorcerers().get(i);
                sb.append(i + 1).append(". ").append(s.getName());
                sb.append(" (").append(s.getRank()).append(")\n");
            }
            sb.append("\n");
        }

        if (mission.getTechniques() != null && !mission.getTechniques().isEmpty()) {
            sb.append("ТЕХНИКИ\n");
            long totalDamage = 0;
            for (int i = 0; i < mission.getTechniques().size(); i++) {
                Technique t = mission.getTechniques().get(i);
                sb.append(i + 1).append(". ").append(t.getName()).append("\n");
                sb.append("   Владелец: ").append(t.getOwner()).append("\n");
                sb.append("   Урон: ").append(formatCurrency(t.getDamage())).append("\n");
                totalDamage += t.getDamage();
            }
            sb.append("\n   Общий урон: ").append(formatCurrency(totalDamage)).append("\n");
        }

        // Дополнительная информация
        if (mission.getExtensions() != null && !mission.getExtensions().isEmpty()) {
            sb.append("\nРАСШИРЕННЫЕ ДАННЫЕ\n");
            for (var entry : mission.getExtensions().entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        }

        return sb.toString();
    }

    private String formatCurrency(long amount) {
        NumberFormat format = NumberFormat.getNumberInstance(new Locale("ru", "RU"));
        return format.format(amount) + " ¥";
    }
}
