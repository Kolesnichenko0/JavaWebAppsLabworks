package csit.semit.kde.javahibernatewebappskdelab2.util.criteria;

import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

@Getter
@ToString
public class TrainQueryParams {
    private String searchingNumber;
    private String searchingArrivalStation;
    private String searchingDepartureStation;
    private Set<MovementType> filteringMovementTypes;
    private LocalTime filteringFrom;
    private LocalTime filteringTo;
    private Duration filteringMinDuration;
    private Duration filteringMaxDuration;
    private Boolean sortedByTrainNumberAsc;
    private Boolean sortedByDurationAsc;
    private Boolean sortedByDepartureTimeAsc;

    private TrainQueryParams (Builder builder) {
        this.searchingNumber = builder.searchingNumber;
        this.searchingArrivalStation = builder.searchingArrivalStation;
        this.searchingDepartureStation = builder.searchingDepartureStation;
        this.filteringMovementTypes = builder.filteringMovementTypes;
        this.filteringFrom = builder.filteringFrom;
        this.filteringTo = builder.filteringTo;
        this.filteringMinDuration = builder.filteringMinDuration;
        this.filteringMaxDuration = builder.filteringMaxDuration;
        this.sortedByTrainNumberAsc = builder.sortedByTrainNumberAsc;
        this.sortedByDurationAsc = builder.sortedByDurationAsc;
        this.sortedByDepartureTimeAsc = builder.sortedByDepartureTimeAsc;
    }

    public static class Builder {
        private String searchingNumber;
        private String searchingArrivalStation;
        private String searchingDepartureStation;
        private Set<MovementType> filteringMovementTypes;
        private LocalTime filteringFrom;
        private LocalTime filteringTo;
        private Duration filteringMinDuration;
        private Duration filteringMaxDuration;
        private Boolean sortedByTrainNumberAsc;
        private Boolean sortedByDurationAsc;
        private Boolean sortedByDepartureTimeAsc;

        public Builder searchingNumber(String searchingNumber) {
            this.searchingNumber = searchingNumber;
            return this;
        }

        public Builder searchingArrivalStation(String searchingArrivalStation) {
            this.searchingArrivalStation = searchingArrivalStation;
            return this;
        }

        public Builder searchingDepartureStation(String searchingDepartureStation) {
            this.searchingDepartureStation = searchingDepartureStation;
            return this;
        }

        public Builder filteringMovementTypes(Set<MovementType> filteringMovementTypes) {
            this.filteringMovementTypes = filteringMovementTypes;
            return this;
        }

        public Builder filteringFrom(LocalTime filteringFrom) {
            this.filteringFrom = filteringFrom;
            return this;
        }

        public Builder filteringTo(LocalTime filteringTo) {
            this.filteringTo = filteringTo;
            return this;
        }

        public Builder filteringMinDuration(Duration filteringMinDuration) {
            this.filteringMinDuration = filteringMinDuration;
            return this;
        }

        public Builder filteringMaxDuration(Duration filteringMaxDuration) {
            this.filteringMaxDuration = filteringMaxDuration;
            return this;
        }

        public Builder sortedByTrainNumberAsc(Boolean sortedByTrainNumberAsc) {
            this.sortedByTrainNumberAsc = sortedByTrainNumberAsc;
            return this;
        }

        public Builder sortedByDurationAsc(Boolean sortedByDurationAsc) {
            this.sortedByDurationAsc = sortedByDurationAsc;
            return this;
        }

        public Builder sortedByDepartureTimeAsc(Boolean sortedByDepartureTimeAsc) {
            this.sortedByDepartureTimeAsc = sortedByDepartureTimeAsc;
            return this;
        }

        public TrainQueryParams build() {
            return new TrainQueryParams(this);
        }
    }
}