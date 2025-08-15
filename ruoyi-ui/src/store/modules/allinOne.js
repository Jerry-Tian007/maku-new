const allinOne = {
    state: {
        botNum: 1,
        modelTypeList: [],
        chatHistoryList: { 0: [], 1: [], 2: [], 3: [] },
        chatMessage: { 0: [], 1: [], 2: [], 3: [] },
        windowModel: {
            0: {
                chatTable: [], chatTableIndex: 1, botType: '', fileMark: '',
                newWordState: false, isNetworkLabel: '', needOnlineSearch: '', sessionId: '', showHistoryState: false, sessionTimeList: [],
                sessionPageNum: 1
            },
            1: {
                chatTable: [], chatTableIndex: 1, botType: '', fileMark: '',
                newWordState: false, isNetworkLabel: '', needOnlineSearch: '', sessionId: '', showHistoryState: false, sessionTimeList: [],
                sessionPageNum: 1
            },
            2: {
                chatTable: [], chatTableIndex: 1, botType: '', fileMark: '',
                newWordState: false, isNetworkLabel: '', needOnlineSearch: '', sessionId: '', showHistoryState: false, sessionTimeList: [],
                sessionPageNum: 1
            },
            3: {
                chatTable: [], chatTableIndex: 1, botType: '', fileMark: '',
                newWordState: false, isNetworkLabel: '', needOnlineSearch: '', sessionId: '', showHistoryState: false, sessionTimeList: [],
                sessionPageNum: 1
            }
        },
        mainWindow: {
            fileList: [],
            currInputTokens: 0,
            currFileTokenNum: 0,
            sendData: ''
        }
    },
    mutations: {
        SET_BOT_NUM: (state, status) => {
            state.botNum = status;
        },
        SET_TYPE_LIST: (state, status) => {
            state.modelTypeList = status;
        },
        SET_HISTORY_LIST: (state, status) => {
            state.chatHistoryList = status;
        },
        SET_MEESAGE: (state, status) => {
            state.chatMessage = status;
        },
        SET_WINDOW: (state, status) => {
            state.windowModel = status;
        },
        SET_MAIN_WINDOWS: (state, status) => {
            state.mainWindow = status;
        }
    },
    actions: {
        setBotNum({ commit }, status) {
            commit('SET_BOT_NUM', status)
        },
        setHistoryList({ commit }, status) {
            commit('SET_HISTORY_LIST', status);
        },
        setTypeList({ commit }, status) {
            commit('SET_TYPE_LIST', status);
        },
        setMessage({ commit }, status) {
            commit('SET_MEESAGE', status);
        },
        setWindowModel({ commit }, status) {
            commit('SET_WINDOW', status);
        },
        setMainWindow({ commit }, status) {
            commit('SET_MAIN_WINDOWS', status);
        }
    }
}

export default allinOne;
