/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.zip.transformer;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.transformer.AbstractTransformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

/**
 * Base class for transformers that provide Zip compression.
 *
 * @author Gunnar Hillert
 * @author Artem Bilan
 * @since 1.0
 */
public abstract class AbstractZipTransformer extends AbstractTransformer {

	private static final Log logger = LogFactory.getLog(ZipTransformer.class);

	protected volatile Charset charset = Charset.defaultCharset();

	protected volatile FileNameGenerator fileNameGenerator;

	protected ZipResultType zipResultType = ZipResultType.FILE;

	protected volatile File workDirectory =
			new File(System.getProperty("java.io.tmpdir") + File.separator + "ziptransformer");

	protected volatile boolean deleteFiles;

	/**
	 * If the payload is an instance of {@link File}, this property specifies
	 * whether to delete the {@link File} after transformation.
	 * Default is <em>false</em>.
	 *
	 * @param deleteFiles Defaults to <em>false</em> if not set
	 */
	public void setDeleteFiles(boolean deleteFiles) {
		this.deleteFiles = deleteFiles;
	}

	/**
	 * Set the work-directory. The work directory is used when the {@link ZipResultType}
	 * is set to {@link ZipResultType#FILE}. By default this property is set to
	 * the System temporary directory containing a sub-directory "ziptransformer".
	 *
	 * @param workDirectory Must not be null and must not represent a file.
	 */
	public void setWorkDirectory(File workDirectory) {
		Assert.notNull(workDirectory, "workDirectory must not be null.");
		Assert.isTrue(!workDirectory.isFile(), "The workDirectory specified must not point to a file");
		this.workDirectory = workDirectory;
	}

	/**
	 * Defines the format of the data returned after transformation. Available
	 * options are:
	 *
	 * <ul>
	 *   <li>File</li>
	 *   <li>Byte Array</li>
	 * </ul>
	 *
	 * Defaults to {@link ZipResultType#FILE}.
	 *
	 * @param zipResultType Must not be null
	 */
	public void setZipResultType(ZipResultType zipResultType) {
		Assert.notNull(zipResultType, "The zipResultType must not be empty.");
		this.zipResultType = zipResultType;
	}

	@Override
	protected void onInit() throws Exception {
		super.onInit();

		if (!this.workDirectory.exists()) {
			if (logger.isInfoEnabled()) {
				logger.info(String.format("Creating work directory '%s'.", this.workDirectory));
			}
			Assert.isTrue(this.workDirectory.mkdirs(), "Can't create the 'workDirectory': " + this.workDirectory);
		}
		final DefaultFileNameGenerator defaultFileNameGenerator = new DefaultFileNameGenerator();
		defaultFileNameGenerator.setBeanFactory(getBeanFactory());
		defaultFileNameGenerator.setConversionService(getConversionService());
		this.fileNameGenerator = defaultFileNameGenerator;

	}

	/**
	 * @param message the message and its payload must not be null.
	 */
	@Override
	protected Object doTransform(Message<?> message) throws Exception {
		Assert.notNull(message, "message must not be null");
		final Object payload = message.getPayload();
		Assert.notNull(payload, "payload must not be null");

		return doZipTransform(message);
	}

	/**
	 * Subclasses must implement this method to provide the Zip transformation
	 * logic.
	 *
	 * @param message The message will never be null.
	 * @return The result of the Zip transformation.
	 * @throws Exception Any exception.
	 */
	protected abstract Object doZipTransform(Message<?> message) throws Exception;

}
