package pl.extollite.hungergames.game;

import pl.extollite.hungergames.hgutils.HGUtils;
import pl.extollite.hungergames.HG;
import pl.extollite.hungergames.data.Language;

/**
 * Game status types
 */
public enum Status {

	/**
	 * Game is running
	 */
	RUNNING,
	/**
	 * Game has stopped
	 */
	STOPPED,
	/**
	 * Game is ready to run
	 */
	READY,
	/**
	 * Game is waiting
	 */
	WAITING,
	/**
	 * Game is broken
	 */
	BROKEN,
	/**
	 * Game is currently rolling back blocks
	 */
	ROLLBACK,
	/**
	 * Game is not ready
	 */
	NOTREADY,
	/**
	 * Game is starting to run
	 */
	BEGINNING,
	/**
	 * Game is counting down to start
	 */
	COUNTDOWN,
	/**
	 * Game is counting down to final
	 */
	FINAL_COUNTDOWN,
	/**
	 * Game is final state
	 */
	FINAL;

	Language lang = HG.getInstance().getLanguage();

	public String getName() {
        switch (this) {
            case RUNNING:
                return HGUtils.colorize(lang.getStatus_running());
            case STOPPED:
                return HGUtils.colorize(lang.getStatus_stopped());
            case READY:
                return HGUtils.colorize(lang.getStatus_ready());
            case WAITING:
                return HGUtils.colorize(lang.getStatus_waiting());
            case BROKEN:
                return HGUtils.colorize(lang.getStatus_broken());
            case ROLLBACK:
                return HGUtils.colorize(lang.getStatus_rollback());
            case NOTREADY:
                return HGUtils.colorize(lang.getStatus_not_ready());
            case BEGINNING:
                return HGUtils.colorize(lang.getStatus_beginning());
            case COUNTDOWN:
                return HGUtils.colorize(lang.getStatus_countdown());
			case FINAL:
				return HGUtils.colorize(lang.getStatus_final());
            default:
                return HGUtils.colorize("&cERROR!");
        }
	}

}
