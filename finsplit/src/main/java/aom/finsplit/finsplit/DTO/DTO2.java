package aom.finsplit.finsplit.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DTO2 {

    private Current current;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public String toString() {
        return this.current.tempC + " " + this.current.tempF;
    }

    public static class Current {
        @JsonProperty("temp_c")
        private double tempC;

        @JsonProperty("temp_f")
        private double tempF;

        public double getTempC() {
            return tempC;
        }

        public void setTempC(double tempC) {
            this.tempC = tempC;
        }

        public double getTempF() {
            return tempF;
        }

        public void setTempF(double tempF) {
            this.tempF = tempF;
        }
    }
}
