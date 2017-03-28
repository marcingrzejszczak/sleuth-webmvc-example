package org.springframework.cloud.sleuth.trace;

import org.springframework.cloud.sleuth.Span;

/**
 * @author Marcin Grzejszczak
 */
public class Trololo {

	public static Span span() {
		return SpanContextHolder.getCurrentSpan();
	}
}
