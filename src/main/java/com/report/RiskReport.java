package com.report;

import com.model.*;

public class RiskReport implements ReportGenerator {

    @Override
    public String generate(Mission mission) {
        StringBuilder sb = new StringBuilder();

        sb.append("Анализ рисков ");

        sb.append("Миссия: ").append(mission.getMissionId()).append("\n");
        sb.append("Локация: ").append(mission.getLocation()).append("\n\n");

        // Риск от проклятия
        int riskScore = 0;

        if (mission.getCurse() != null) {
            sb.append("УРОВЕНЬ УГРОЗЫ ПРОКЛЯТИЯ\n");
            String threat = mission.getCurse().getThreatLevel();

            switch (threat) {
                case "SPECIAL_GRADE":
                    sb.append("ОСОБЫЙ РАНГ (Критический риск)\n");
                    sb.append("  → Требуется немедленная эвакуация гражданских\n");
                    sb.append("  → Необходимо привлечение магов особого ранга\n");
                    riskScore += 10;
                    break;
                case "HIGH":
                    sb.append("ВЫСОКИЙ (Высокий риск)\n");
                    sb.append("  → Рекомендовано усиление магического барьера\n");
                    riskScore += 7;
                    break;
                case "MEDIUM":
                    sb.append("СРЕДНИЙ (Умеренный риск)\n");
                    riskScore += 4;
                    break;
                case "LOW":
                    sb.append("НИЗКИЙ (Низкий риск)\n");
                    riskScore += 2;
                    break;
            }
            sb.append("\n");
        }

        // Риск от результата миссии
        sb.append("РЕЗУЛЬТАТ ОПЕРАЦИИ\n");
        if ("FAILURE".equals(mission.getOutcome())) {
            sb.append("МИССИЯ ПРОВАЛЕНА\n");
            sb.append("Риск повторной активации проклятия: ВЫСОКИЙ\n");
            sb.append("Требуется повторная операция\n");
            riskScore += 8;
        } else {
            sb.append("МИССИЯ УСПЕШНА\n");
            sb.append("Риск рецидива: НИЗКИЙ\n");
        }
        sb.append("\n");

        // Экономический риск
        sb.append("ЭКОНОМИЧЕСКАЯ ОЦЕНКА\n");
        long damage = mission.getDamageCost();
        if (damage > 5000000) {
            sb.append("  → Сумма ущерба: ").append(formatCurrency(damage)).append("\n");
            sb.append("  → Превышен бюджетный лимит\n");
            riskScore += 5;
        } else if (damage > 1000000) {
            sb.append("  → Сумма ущерба: ").append(formatCurrency(damage)).append("\n");
            riskScore += 3;
        } else {
            sb.append("  → Ущерб в пределах нормы: ").append(formatCurrency(damage)).append("\n");
        }
        sb.append("\n");

        // Итоговая оценка риска
        sb.append("ИТОГОВАЯ ОЦЕНКА РИСКА: ");

        if (riskScore >= 20) {
            sb.append("КРИТИЧЕСКИЙ (Требуется экстренное вмешательство)\n");
        } else if (riskScore >= 15) {
            sb.append("ВЫСОКИЙ (Рекомендовано усиление мер безопасности)\n");
        } else if (riskScore >= 10) {
            sb.append("СРЕДНИЙ (Стандартный протокол)\n");
        } else {
            sb.append("НИЗКИЙ (Операция проведена штатно)\n");
        }

        sb.append("Общий балл риска: ").append(riskScore).append("/30\n");

        return sb.toString();
    }

    private String formatCurrency(long amount) {
        return String.format("%,d ¥", amount);
    }
}
