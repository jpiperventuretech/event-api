/*
 * Copyright (c) Interactive Information R & D (I2RD) LLC.
 * All Rights Reserved.
 *
 * This software is confidential and proprietary information of
 * I2RD LLC ("Confidential Information"). You shall not disclose
 * such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with I2RD.
 */

package co.proteus.events.publication;

/**
 * The status of {@link PublisherService#publish a published event}
 *
 * @author Justin Piper (jpiper@proteus.co)
 */
public enum PublishStatus
{
    /** The message was published */
    PUBLISHED,
    /** The message was not published due to a problem with the IoT client */
    FAILED,
    /** The message was not published because the request timed out */
    TIMED_OUT,
    /** The message was not published because it was throttled */
    THROTTLED
}
