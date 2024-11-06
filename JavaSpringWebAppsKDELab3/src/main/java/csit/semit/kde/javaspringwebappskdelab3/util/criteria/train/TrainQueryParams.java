package csit.semit.kde.javaspringwebappskdelab3.util.criteria.train;

import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

/**
 * Class representing query parameters for searching, filtering, and sorting train entities.
 * <p>
 * This class is used to encapsulate various parameters that can be used to search for, filter, and sort train entities.
 * It includes parameters for searching by train number, departure station, and arrival station, as well as filtering by movement types,
 * departure times, and durations. Additionally, it supports sorting by train number, duration, and departure time.
 * </p>
 * <p>
 * The `TrainQueryParams` class includes:
 * <ul>
 *   <li>Search parameters: {@code searchingNumber}, {@code searchingArrivalStation}, {@code searchingDepartureStation}</li>
 *   <li>Filter parameters: {@code filteringMovementTypes}, {@code filteringFrom}, {@code filteringTo}, {@code filteringMinDuration}, {@code filteringMaxDuration}</li>
 *   <li>Sort parameters: {@code sortedByTrainNumberAsc}, {@code sortedByDurationAsc}, {@code sortedByDepartureTimeAsc}</li>
 * </ul>
 * </p>
 * <p>
 * The class uses the Builder pattern to facilitate the construction of `TrainQueryParams` objects.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see MovementType
 * @see Duration
 * @see LocalTime
 * @since 1.0.0
 */
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

    private TrainQueryParams(Builder builder) {
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

    public boolean isEmpty() {
        return searchingNumber == null && searchingArrivalStation == null && searchingDepartureStation == null
                && filteringMovementTypes == null && filteringFrom == null && filteringTo == null
                && filteringMinDuration == null && filteringMaxDuration == null
                && sortedByTrainNumberAsc == null && sortedByDurationAsc == null && sortedByDepartureTimeAsc == null;
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