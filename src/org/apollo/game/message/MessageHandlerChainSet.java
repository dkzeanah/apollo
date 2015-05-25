package org.apollo.game.message;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.model.entity.Player;

/**
 * A group of {@link MessageHandlerChain}s classified by the {@link Message} type.
 *
 * @author Graham
 * @author Ryley
 * @author Major
 */
public final class MessageHandlerChainSet {

	/**
	 * The {@link Map} of Message types to {@link MessageHandlerChain}s
	 */
	private final Map<Class<? extends Message>, MessageHandlerChain<? extends Message>> chains = new HashMap<>();

	/**
	 * Notifies the appropriate {@link MessageHandlerChain} that a {@link Message} has been received.
	 *
	 * @param player The {@link Player} receiving the Message.
	 * @param message The Message.
	 * @return {@code true} if the Message propagated down the chain without being terminated or if the chain for the
	 *         Message was not found, otherwise {@code false}.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Message> boolean notify(Player player, M message) {
		Class<M> clazz = (Class<M>) message.getClass();
		while (clazz.getSuperclass() != Message.class) {
			clazz = (Class<M>) clazz.getSuperclass();
		}

		MessageHandlerChain<M> chain = (MessageHandlerChain<M>) chains.computeIfAbsent(clazz, MessageHandlerChain::new);
		return chain.notify(player, message);
	}

	/**
	 * Places the {@link MessageHandlerChain} into this set.
	 *
	 * @param clazz The {@link Class} to associate the MessageHandlerChain with.
	 * @param handler The MessageHandlerChain.
	 */
	@SuppressWarnings("unchecked")
	public <M extends Message> void putHandler(Class<M> clazz, MessageHandler<? extends Message> handler) {
		MessageHandlerChain<M> chain = (MessageHandlerChain<M>) chains.computeIfAbsent(clazz, MessageHandlerChain::new);
		chain.addHandler((MessageHandler<M>) handler);
	}

}