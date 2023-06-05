FROM --platform=linux/amd64 bellsoft/liberica-openjdk-debian:11 AS openjdk11
FROM --platform=linux/amd64 adoptopenjdk/openjdk9:latest AS openjdk9

FROM --platform=linux/amd64 gradle/gradle-enterprise-test-distribution-agent:2.0.1

ENV JDK11=/opt/java/openjdk-11
COPY --from=openjdk11 /usr/lib/jvm/jdk ${JDK11}

ENV JDK9=/opt/java/openjdk-9
ENV JDK_9=${JDK9}
COPY --from=openjdk9 /opt/java/openjdk ${JDK9}

ENV TEST_DISTRIBUTION_AGENT_SERVER=https://ec2-3-237-201-221.compute-1.amazonaws.com
ENV TEST_DISTRIBUTION_AGENT_ALLOW_UNTRUSTED=true
ENV TEST_DISTRIBUTION_AGENT_CAPABILITIES=my-local-agent,android
ENV TEST_DISTRIBUTION_AGENT_API_KEY=cynkhriekpan6vqmfcrjfmkelhbvosxyjkvffeuehz6nung65t5a

# Android SDK installation

USER 0
RUN  apt-get update \
  && apt-get install -y wget \
  && apt-get install -y unzip \
  && rm -rf /var/lib/apt/lists/*
USER gradle
RUN mkdir -p /home/gradle/android-sdk

ENV ANDROID_HOME "/home/gradle/android-sdk"
ENV ANDROID_SDK_ROOT $ANDROID_HOME
ENV CMDLINE_TOOLS_ROOT "${ANDROID_HOME}/cmdline-tools/latest/bin"
ENV ADB_INSTALL_TIMEOUT 120
ENV PATH "${ANDROID_HOME}/emulator:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/tools:${ANDROID_HOME}/tools/bin:${ANDROID_HOME}/platform-tools:${ANDROID_HOME}/platform-tools/bin:${PATH}"
# You can find the latest command line tools here: https://developer.android.com/studio#command-line-tools-only
RUN SDK_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip" && \
	mkdir -p ${ANDROID_HOME}/cmdline-tools && \
	mkdir ${ANDROID_HOME}/platforms && \
	mkdir ${ANDROID_HOME}/ndk && \
	wget -O /tmp/cmdline-tools.zip -t 5 "${SDK_TOOLS_URL}" && \
	unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
	rm /tmp/cmdline-tools.zip && \
	mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest

RUN echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "tools" && \
	echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "platform-tools" && \
	echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "build-tools;34.0.0-rc2"

RUN echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "platforms;android-UpsideDownCake" && \
	echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "platforms;android-33" && \
	echo y | ${CMDLINE_TOOLS_ROOT}/sdkmanager "platforms;android-32"
